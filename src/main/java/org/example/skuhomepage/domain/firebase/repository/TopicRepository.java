package org.example.skuhomepage.domain.firebase.repository;

import java.util.Optional;

import org.example.skuhomepage.domain.firebase.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

  Optional<Topic> findByTopicAndTopicGroup(String topic, String topicGroup);
}
