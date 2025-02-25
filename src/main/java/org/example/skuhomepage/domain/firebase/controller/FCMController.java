package org.example.skuhomepage.domain.firebase.controller;

import org.example.skuhomepage.domain.firebase.dto.TokenRequestDTO;
import org.example.skuhomepage.domain.firebase.dto.TopicRequestDTO;
import org.example.skuhomepage.domain.firebase.service.FCMService;
import org.example.skuhomepage.domain.firebase.service.FCMTopicService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FCMController implements FCMControllerSpec {

  private final FCMService fcmService;
  private final FCMTopicService fcmTopicService;

  @Override
  public ApiResponse<Void> registerToken(TokenRequestDTO tokenReq) {

    fcmService.registerToken(tokenReq);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<Void> sendTestMessage(TokenRequestDTO tokenReq) {
    fcmService.sendTestMessage(tokenReq);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<Void> sendTestTopicMessage(TopicRequestDTO topicReq) {
    fcmService.sendTestTopicMessage(topicReq);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<Void> topicRegister(TopicRequestDTO topicReq) {
    fcmTopicService.registerTopic(topicReq, 1L);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<Void> topicDelete(TopicRequestDTO topicReq) {
    fcmTopicService.deleteTopic(topicReq, 1L);
    return ApiResponse.onSuccess(null);
  }
}
