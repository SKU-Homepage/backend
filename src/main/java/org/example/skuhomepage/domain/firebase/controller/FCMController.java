package org.example.skuhomepage.domain.firebase.controller;

import org.example.skuhomepage.domain.firebase.dto.TokenRequestDTO;
import org.example.skuhomepage.domain.firebase.dto.TopicRequestDTO;
import org.example.skuhomepage.domain.firebase.service.DeviceTokenService;
import org.example.skuhomepage.domain.firebase.service.KeywordService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FCMController implements FCMControllerSpec {

  // private final FCMService fcmService;
  private final DeviceTokenService deviceTokenService;
  private final KeywordService keywordService;

  // private final FCMTopicService fcmTopicService;

  @Override
  public ApiResponse<Void> registerToken(TokenRequestDTO tokenReq, CustomUserDetails userDetails) {

    deviceTokenService.registerToken(tokenReq, userDetails);
    return ApiResponse.onSuccess(null);
  }

  //  @Override
  //  public ApiResponse<Void> sendTestMessage(
  //      TokenRequestDTO tokenReq, CustomUserDetails userDetails) {
  //    fcmService.sendTestMessage(tokenReq);
  //    return ApiResponse.onSuccess(null);
  //  }
  //
  //  @Override
  //  public ApiResponse<Void> sendTestTopicMessage(
  //      TopicRequestDTO topicReq, CustomUserDetails userDetails) {
  //    fcmService.sendTestTopicMessage(topicReq);
  //    return ApiResponse.onSuccess(null);
  //  }

  @Override
  public ApiResponse<Void> topicRegister(TopicRequestDTO topicReq, CustomUserDetails userDetails) {
    keywordService.registerKeyword(topicReq, userDetails);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<Void> topicDelete(String keyword, CustomUserDetails userDetails) {
    keywordService.deleteKeyword(keyword, userDetails);
    return ApiResponse.onSuccess(null);
  }
  //
  //  @Override
  //  public ApiResponse<Void> topicList(TopicGroup topicGroup, CustomUserDetails userDetails) {
  //    return ApiResponse.onSuccess(null);
  //  }
}
