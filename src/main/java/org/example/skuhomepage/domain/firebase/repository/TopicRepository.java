package org.example.skuhomepage.domain.firebase.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.firebase.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Long> {

  Optional<Topic> findByTopicAndTopicGroup(String topic, String topicGroup);

  @Query("SELECT t.topic FROM Topic t WHERE t.topicGroup = :topicGroup")
  List<String> findTopicsByTopicGroup(String topicGroup);
}
