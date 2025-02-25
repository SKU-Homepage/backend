package org.example.skuhomepage.domain.mypage.controller;


import org.example.skuhomepage.domain.mypage.dto.MyPageRequestDTO.signupRequestDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.MyPageInfoDTO;

import java.time.LocalDateTime;

import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.LoginResultDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.MyPageInfoDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.SignupResultDTO;

import org.example.skuhomepage.domain.mypage.service.MyPageService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MyPageController implements MyPageControllerSpec {

  private final MyPageService mypageService;

  @Override
  public ApiResponse<MyPageInfoDTO> getMyInfo(UserDetails userDetails) {
    MyPageResponseDTO.MyPageInfoDTO result = mypageService.getMyInfo(userDetails);
    return ApiResponse.onSuccess(
        new MyPageInfoDTO("김서경", "예술대학", "디자인학부", "전공", "2021418008", "4학년", "재학"));
  }

  @Override
  public ApiResponse<Void> signUp(signupRequestDTO request, UserDetails userDetails) {
    mypageService.signUp(userDetails, request);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<MyPageResponseDTO.LoginResultDTO> googleLogin(String code, int env) {
    MyPageResponseDTO.LoginResultDTO result = mypageService.googleLogin(code, env);
    return ApiResponse.onSuccess(result);

  }
}
