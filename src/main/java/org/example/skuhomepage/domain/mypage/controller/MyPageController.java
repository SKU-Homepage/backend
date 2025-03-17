package org.example.skuhomepage.domain.mypage.controller;

import org.example.skuhomepage.domain.mypage.dto.MyPageRequestDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.MyPageInfoDTO;
import org.example.skuhomepage.domain.mypage.service.MyPageService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MyPageController implements MyPageControllerSpec {

  private final MyPageService mypageService;

  @Override
  public ApiResponse<MyPageInfoDTO> getMyInfo(UserDetails userDetails) {
    MyPageResponseDTO.MyPageInfoDTO result = mypageService.getMyInfo(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<MyPageResponseDTO.signUpResultDTO> signUp(
      MyPageRequestDTO.signupRequestDTO request, UserDetails userDetails) {
    MyPageResponseDTO.signUpResultDTO result = mypageService.signUp(userDetails, request);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<MyPageResponseDTO.LoginResultDTO> googleLogin(String code, String env) {
    MyPageResponseDTO.LoginResultDTO result = mypageService.googleLogin(code, env);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<MyPageResponseDTO.RegisterDTO> registeredUsers(UserDetails userDetails) {
    MyPageResponseDTO.RegisterDTO result = mypageService.isRegistered(userDetails);
    return ApiResponse.onSuccess(result);
  }
}
