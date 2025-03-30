package org.example.skuhomepage.domain.firebase.controller;

import org.example.skuhomepage.domain.firebase.dto.NotificationResponseDTO;
import org.example.skuhomepage.domain.firebase.dto.TokenRequestDTO;
import org.example.skuhomepage.domain.firebase.dto.TopicRequestDTO;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "알림", description = "FCM 알림 관련 API")
@RequestMapping("/api/fcm")
public interface FCMControllerSpec {

  @PostMapping("/register")
  @Operation(summary = "토큰 등록 및 만료 기간 연장", description = "FCM 토큰을 등록하거나 만료 기간을 연장하는 API")
  ApiResponse<Void> registerToken(
      @RequestBody TokenRequestDTO req, @AuthenticationPrincipal CustomUserDetails userDetails);

  //  @PostMapping("/test/send-token")
  //  @Operation(summary = "테스트 알림 전송", description = "테스트 알림을 전송하는 API")
  //  ApiResponse<Void> sendTestMessage(
  //      @RequestBody TokenRequestDTO req, @AuthenticationPrincipal CustomUserDetails userDetails);
  //
  //  @PostMapping("/test/send-topic")
  //  @Operation(summary = "테스트 토픽 알림 전송", description = "테스트 토픽 알림을 전송하는 API")
  //  ApiResponse<Void> sendTestTopicMessage(
  //      @RequestBody TopicRequestDTO req, @AuthenticationPrincipal CustomUserDetails userDetails);
  @GetMapping("/alarm")
  @Operation(summary = "알림 조회", description = "사용자가 받은 알림들 조회 api")
  ApiResponse<NotificationResponseDTO.NotificationListDTO> getAlarmList(
      @AuthenticationPrincipal CustomUserDetails userDetails);

  @PostMapping("/keyword")
  @Operation(summary = "키워드 등록", description = "키워드를 등록하는 API")
  ApiResponse<Void> topicRegister(
      @RequestBody TopicRequestDTO keywordReq,
      @AuthenticationPrincipal CustomUserDetails userDetails);

  @DeleteMapping("/keyword/{keywordId}")
  @Operation(summary = "키워드 삭제", description = "키워드를 삭제하는 API")
  ApiResponse<Void> topicDelete(
      @Parameter(description = "삭제할 키워드", example = "1") @PathVariable Long keywordId,
      @AuthenticationPrincipal CustomUserDetails userDetails);

  @GetMapping("/keyword")
  @Operation(summary = "키워드 조회", description = "키워드를 조회하는 API")
  ApiResponse<NotificationResponseDTO.keywordDTO> topicList(
      @Parameter(description = "사용자 정보", hidden = true) @AuthenticationPrincipal
          CustomUserDetails userDetails);
}
