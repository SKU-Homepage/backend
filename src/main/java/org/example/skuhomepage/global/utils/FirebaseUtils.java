package org.example.skuhomepage.global.utils;

import java.util.Collections;
import java.util.List;

import org.example.skuhomepage.domain.firebase.service.NotificationSubscribeService;
import org.example.skuhomepage.global.dto.MessageRequest;
import org.springframework.stereotype.Service;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.firebase.messaging.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class FirebaseUtils {

  private final NotificationSubscribeService notificationSubscribeService;

  public void sendMessage(MessageRequest msgReq) {

    Message message = msgReq.toMessage();

    try {
      String response = FirebaseMessaging.getInstance().send(message);
      log.info("메시지가 성공적으로 전송되었습니다: {}", response);
    } catch (FirebaseMessagingException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendTopicMessage(MessageRequest msgReq) {

    Message message = msgReq.toMessage();

    try {
      String response = FirebaseMessaging.getInstance().send(message);
      log.info("메시지가 성공적으로 전송되었습니다: {}", response);
    } catch (FirebaseMessagingException e) {
      throw new RuntimeException(e);
    }
  }

  public void topicSubscribe(String topic, String token) {

    notificationSubscribeService.addTokenForNotificationSubscribe(topic, token);
    List<String> tokens = notificationSubscribeService.getTokenListByTopic(topic);

    try {
      TopicManagementResponse response =
          FirebaseMessaging.getInstance().subscribeToTopic(tokens, topic);
      log.info("토픽 구독이 성공적으로 완료되었습니다: {}", response.getSuccessCount());
    } catch (InvalidArgumentException e) {
      log.error("토픽 구독이 실패했습니다: {}", e.getMessage());
      throw new RuntimeException(e);
    } catch (FirebaseMessagingException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e + token + "토픽 구독이 실패했습니다");
    }
  }

  public void topicUnsubscribe(String topic, List<String> tokens) {
    for (String token : tokens) {
      notificationSubscribeService.deleteTokenForNotificationSubscribe(topic, token);
    }

    try {
      TopicManagementResponse response =
          FirebaseMessaging.getInstance().unsubscribeFromTopic(tokens, topic);
      log.info("토픽 구독 해지가 성공적으로 완료되었습니다: {}", response.getSuccessCount());
    } catch (FirebaseMessagingException e) {
      throw new RuntimeException(e);
    }
  }

  public void unsubscribeAllTopicByToken(String token) {
    List<String> topics = notificationSubscribeService.getTopicListByToken(token);
    topics.forEach(topic -> topicUnsubscribe(topic, Collections.singletonList(token)));
  }
}
