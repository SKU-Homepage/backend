package org.example.skuhomepage.domain.firebase.repository;

import java.util.List;

import org.example.skuhomepage.domain.firebase.entity.NotificationSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationSubscribeRepository
    extends JpaRepository<NotificationSubscribe, Long> {

  @Query("SELECT n.token FROM NotificationSubscribe n WHERE n.topic = :topic")
  List<String> findTokenByTopic(String topic);

  @Query("SELECT n.topic FROM NotificationSubscribe n WHERE n.token = :token")
  List<String> findTopicByToken(String token);

  List<NotificationSubscribe> findByUserId(Long userId);

  void deleteByTopicAndToken(String topic, String token);

  void deleteByTopicAndUserId(String topic, Long userId);

  boolean existsByTopicAndToken(String topic, String token);
}
