package org.example.skuhomepage.domain.firebase.service;

import java.util.List;

import org.example.skuhomepage.domain.firebase.entity.NotificationSubscribe;
import org.example.skuhomepage.domain.firebase.repository.NotificationSubscribeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationSubscribeService {

  private final NotificationSubscribeRepository notificationSubscribeRepository;

  public List<String> getTokenListByTopic(String topic) {
    return notificationSubscribeRepository.findTokenByTopic(topic);
  }

  public List<String> getTopicListByToken(String token) {
    return notificationSubscribeRepository.findTopicByToken(token);
  }

  @Transactional
  public void deleteTokenForNotificationSubscribe(String topic, String token) {
    try {
      notificationSubscribeRepository.deleteByTopicAndToken(topic, token);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteNotificationSubscribeByTopicAndUserId(String topic, long userId) {
    try {
      notificationSubscribeRepository.deleteByTopicAndUserId(topic, userId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void addTokenForNotificationSubscribe(String topic, String token) {
    try {

      if (notificationSubscribeRepository.existsByTopicAndToken(topic, token)) {
        return;
      }

      notificationSubscribeRepository.save(
          NotificationSubscribe.builder().topic(topic).token(token).build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
