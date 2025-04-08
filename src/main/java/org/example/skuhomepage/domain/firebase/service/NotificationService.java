package org.example.skuhomepage.domain.firebase.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.firebase.dto.NotificationRequestDTO;
import org.example.skuhomepage.domain.firebase.dto.NotificationResponseDTO;
import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final FirebaseMessaging firebaseMessaging;
  private final AlarmRepository alarmRepository;
  private final UserRepository userRepository;

  public void sendPush(String token, String title, String body, String redirectPath) {
    Message message =
        Message.builder()
            .setToken(token)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .putData("redirect", redirectPath)
            .build();

    try {
      String response = firebaseMessaging.send(message);
      System.out.println("푸시 알림이 전송되었습니다. 토큰: " + token + ": " + response);
    } catch (FirebaseMessagingException e) {
      System.err.println("푸시 알림 전송 실패: " + token + " - " + e.getMessage());
    }
  }

  public NotificationResponseDTO.NotificationListDTO getNotificationList(UserDetails userDetails) {

    List<Alarm> alarms =
        alarmRepository.findAllByUser_AccountOrderByCreatedAtDesc(userDetails.getUsername());

    List<NotificationResponseDTO.NotificationDTO> notificationDTOList =
        alarms.stream()
            .map(
                notification ->
                    new NotificationResponseDTO.NotificationDTO(
                        notification.getId(),
                        notification.getContent(),
                        notification.getNotificationType().getRedirectUrl(),
                        notification.getCreatedDate(),
                        notification.isRead()))
            .collect(Collectors.toList());

    return new NotificationResponseDTO.NotificationListDTO(notificationDTOList);
  }

  public void deleteAlarm(
      NotificationRequestDTO.deleteAlarmDTO request, CustomUserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    List<Long> alarmIds = request.getAlarmIds();

    List<Alarm> alarmsToDelete =
        alarmRepository.findAllById(alarmIds).stream()
            .filter(alarm -> alarm.getUser().getId().equals(user.getId()))
            .collect(Collectors.toList());

    alarmRepository.deleteAll(alarmsToDelete);
  }

  public NotificationResponseDTO.alarmDTO getAlarm(CustomUserDetails userDetails, Long alarmId) {
    Alarm alarm =
        alarmRepository
            .findByIdAndUser_Account(alarmId, userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("해당 알림을 찾을 수 없습니다."));

    if (!alarm.isRead()) alarm.setRead(true);

    return new NotificationResponseDTO.alarmDTO("/notice");
  }
}
