package org.example.skuhomepage.domain.firebase.service;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class FCMService {
//
//  private final FirebaseUtils firebaseUtils;
//  private final RedisUtils redisUtils;
//  private final FCMTopicService fcmTopicService;
//  private final NotificationSubscribeRepository notificationSubscribeRepository;
//
//  public void registerToken(TokenRequestDTO tokenReq, Long userId) {
//    redisUtils.saveTokenWithExpiry(userId, tokenReq.getToken(), 60 * 60);
//
//    notificationSubscribeRepository
//        .findByUserId(userId)
//        .forEach(
//            notificationSubscribe ->
//                firebaseUtils.topicSubscribe(
//                    notificationSubscribe.getTopic(), tokenReq.getToken()));
//  }
//
//  public void sendTestMessage(TokenRequestDTO tokenReq) {
//    firebaseUtils.sendMessage(
//        MessageRequest.builder()
//            .title("테스트 알림입니다.")
//            .content("테스트 알림 내용입니다.")
//
// .contentUrl("https://www.skuniv.ac.kr/index.php?mid=notice&page=1&document_srl=266092")
//            .inAppLink("/notice/266092")
//            .sendTime(LocalDateTime.now())
//            .token(tokenReq.getToken())
//            .build());
//  }
//
//  public void sendTestTopicMessage(TopicRequestDTO topicReq) {
//    firebaseUtils.sendTopicMessage(
//        MessageRequest.builder()
//            .title("테스트 알림입니다.")
//            .content("테스트 알림 내용입니다.")
//
// .contentUrl("https://www.skuniv.ac.kr/index.php?mid=notice&page=1&document_srl=266092")
//            .inAppLink("/notice/266092")
//            .sendTime(LocalDateTime.now())
//            .topic(fcmTopicService.topicCreator(topicReq.getTopicGroup(), topicReq.getKeyword()))
//            .build());
//  }
//
//  public void sendTopicMessage(MessageRequest msgRequest, TopicRequestDTO topicReq) {
//    firebaseUtils.sendTopicMessage(
//        MessageRequest.builder()
//            .title(msgRequest.getTitle())
//            .content(msgRequest.getContent())
//            .contentUrl(msgRequest.getContentUrl())
//            .inAppLink(msgRequest.getInAppLink())
//            .sendTime(LocalDateTime.now())
//            .topic(fcmTopicService.topicCreator(topicReq.getTopicGroup(), topicReq.getKeyword()))
//            .build());
//
//    log.info("토픽 메시지 전송 완료: {}", msgRequest.getTopic());
//  }
// }
