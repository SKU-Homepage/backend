package org.example.skuhomepage.domain.firebase.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.firebase.dto.PushBatchMessage;
import org.example.skuhomepage.domain.firebase.dto.UserPushItem;
import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.example.skuhomepage.domain.firebase.service.NotificationService;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushBatchConsumer {

  private final UserRepository userRepository;
  private final AlarmRepository alarmRepository;
  private final NotificationService notificationService;
  private final JobTracker jobTracker;

  private static final Logger log = LoggerFactory.getLogger(PushBatchConsumer.class);

  @RabbitListener(queues = "push.queue", concurrency = "4")
  public void consume(PushBatchMessage message) {
    StopWatch stopWatch = new StopWatch("Batch Consume: " + message.getItems().size() + " items");
    stopWatch.start();

    List<UserPushItem> items = message.getItems();
    if (items.isEmpty()) {
      log.info("Received an empty batch.");
      return;
    }

    // 1. 메시지에서 모든 userId 추출
    List<Long> userIds = items.stream().map(UserPushItem::getUserId).collect(Collectors.toList());

    // 2. Fetch Join을 사용하여 User와 UserDeviceToken 정보를 한 번에 조회
    //    (N+1 문제 해결!)
    List<User> usersWithTokens = userRepository.findUsersWithDeviceTokensByIds(userIds);

    // 3. User ID를 키로 하는 Map으로 변환하여 빠른 조회를 위함
    Map<Long, User> userMap =
        usersWithTokens.stream().collect(Collectors.toMap(User::getId, user -> user));

    List<Alarm> alarmsToSave = new ArrayList<>();

    // 4. DB 조회 없이 메모리에서 데이터 처리
    for (UserPushItem item : items) {
      User user = userMap.get(item.getUserId());

      // User가 존재하고, 해당 User의 DeviceToken 목록이 비어있지 않은 경우
      if (user != null && !user.getUserDeviceTokens().isEmpty()) {
        // 여러 토큰 중 어떤 것을 사용할지 결정 (예: 첫 번째 토큰)
        UserDeviceToken token = user.getUserDeviceTokens().get(0);

        // 푸시 알림 전송
        notificationService.sendPush(token.getFcmToken(), item.getTitle(), item.getBody(), "/");

        // 알람 객체 생성
        //        Alarm alarm =
        //            Alarm.builder()
        //                .user(user)
        //                .title(item.getTitle())
        //                .content(item.getBody())
        //                .notificationType(NotificationType.CALENDAR)
        //                .build();
        //        alarmsToSave.add(alarm);
      } else {
        log.warn("User or Token not found for userId: {}", item.getUserId());
      }
    }

    // 5. 모든 알람을 한 번에 저장 (Bulk Insert)
    if (!alarmsToSave.isEmpty()) {
      alarmRepository.saveAll(alarmsToSave);
    }

    stopWatch.stop();
    log.info("Batch processing finished. Duration: {} ms", stopWatch.getTotalTimeMillis());

    jobTracker.completeOne();
  }
}
