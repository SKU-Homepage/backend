package org.example.skuhomepage.domain.firebase.controller;

import org.example.skuhomepage.domain.firebase.dto.TokenRequestDTO;
import org.example.skuhomepage.domain.firebase.dto.TopicRequestDTO;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "알림", description = "FCM 알림 관련 API")
@RequestMapping("/api/fcm")
public interface FCMControllerSpec {

  @PostMapping("/register")
  @Operation(summary = "토큰 등록 및 만료 기간 연장", description = "FCM 토큰을 등록하거나 만료 기간을 연장하는 API")
  ApiResponse<Void> registerToken(@RequestBody TokenRequestDTO req);

  @PostMapping("/test/send-token")
  @Operation(summary = "테스트 알림 전송", description = "테스트 알림을 전송하는 API")
  ApiResponse<Void> sendTestMessage(@RequestBody TokenRequestDTO req);

  @PostMapping("/test/send-topic")
  @Operation(summary = "테스트 토픽 알림 전송", description = "테스트 토픽 알림을 전송하는 API")
  ApiResponse<Void> sendTestTopicMessage(@RequestBody TopicRequestDTO req);

  @PostMapping("/keyword")
  @Operation(summary = "키워드 등록", description = "키워드를 등록하는 API")
  ApiResponse<Void> topicRegister(@RequestBody TopicRequestDTO keywordReq);

  @DeleteMapping("/keyword")
  @Operation(summary = "키워드 삭제", description = "키워드를 삭제하는 API")
  ApiResponse<Void> topicDelete(@RequestBody TopicRequestDTO keywordReq);
}
