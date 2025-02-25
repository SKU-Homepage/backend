package org.example.skuhomepage.domain.firebase.service;

import java.util.Set;

import org.example.skuhomepage.domain.firebase.dto.TopicRequestDTO;
import org.example.skuhomepage.domain.firebase.entity.Topic;
import org.example.skuhomepage.domain.firebase.repository.TopicRepository;
import org.example.skuhomepage.global.enums.TopicGroup;
import org.example.skuhomepage.global.utils.FirebaseUtils;
import org.example.skuhomepage.global.utils.RedisUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FCMTopicService {

  private final TopicRepository topicRepository;
  private final FirebaseUtils firebaseUtils;
  private final RedisUtils redisUtils;

  public Topic saveTopic(String topic, TopicGroup topicGroup) {

    return topicRepository.save(
        Topic.builder().topic(topic).topicGroup(topicGroup.getValue()).build());
  }

  public void registerTopic(TopicRequestDTO topicReq, long userId) {
    getOrCreateTopicId(topicReq.getKeyword(), topicReq.getTopicGroup());
    Set<String> tokens = redisUtils.getTokens(userId);

    String topicName = topicCreator(topicReq.getTopicGroup(), topicReq.getKeyword());

    for (String token : tokens) {
      firebaseUtils.topicSubscribe(topicName, token);
    }
  }

  public void deleteTopic(TopicRequestDTO topicReq, long userId) {

    Set<String> tokens = redisUtils.getTokens(userId);
    String topicName = topicCreator(topicReq.getTopicGroup(), topicReq.getKeyword());
    firebaseUtils.topicUnsubscribe(topicName, tokens.stream().toList());
  }

  public Long getOrCreateTopicId(String topic, TopicGroup topicGroup) {

    return topicRepository
        .findByTopicAndTopicGroup(topic, topicGroup.getValue())
        .map(Topic::getId)
        .orElseGet(() -> saveTopic(topic, topicGroup).getId());
  }

  public String topicCreator(TopicGroup topicGroup, String topicContent) {
    Long topicId = getOrCreateTopicId(topicContent, topicGroup);
    return topicGroup.getValue() + "-" + topicId.toString();
  }

  public String topicCreator(TopicGroup topicGroup) {
    return topicGroup.getValue();
  }
}
