package org.example.skuhomepage.domain.mypage.controller;

import org.example.skuhomepage.domain.mypage.dto.MyPageRequestDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "mypage-controller", description = "회원 관련 로직 API")
@RequestMapping("/api/mypage/sku")
public interface MyPageControllerSpec {
  @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회하는 API")
  @ApiErrorCodeExample(MyPageErrorStatus.class)
  @GetMapping
  ApiResponse<MyPageResponseDTO.MyPageInfoDTO> getMyInfo(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @Operation(summary = "회원가입", description = "회원가입 API")
  @ApiErrorCodeExample(MyPageErrorStatus.class)
  @PostMapping("/signup")
  ApiResponse<MyPageResponseDTO.signUpResultDTO> signUp(
      @RequestBody MyPageRequestDTO.signupRequestDTO request,
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @Operation(summary = "구글 소셜로그인", description = "구글 소셜로그인 API")
  @ApiErrorCodeExample(MyPageErrorStatus.class)
  @GetMapping("/oauth2/code/google")
  ApiResponse<MyPageResponseDTO.LoginResultDTO> googleLogin(
      @RequestParam(name = "code") String code,
      @RequestParam(name = "env", required = false, defaultValue = "1") String env);

  @Operation(summary = "회원가입 여부 조회", description = "회원가입 여부 정보 조회 api")
  @ApiErrorCodeExample(MyPageErrorStatus.class)
  @GetMapping("/registered")
  ApiResponse<MyPageResponseDTO.RegisterDTO> registeredUsers(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);
}
