package org.example.skuhomepage.domain.mypage.controller;

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
  public ApiResponse<SignupResultDTO> signUp(UserDetails userDetails) {
    MyPageResponseDTO.SignupResultDTO result = mypageService.signUp(userDetails);
    return ApiResponse.onSuccess(
        new SignupResultDTO("김서경", "예술대학", "디자인학부", "전공", "2021418008", "4학년", "재학"));
  }

  @Override
  public ApiResponse<LoginResultDTO> googleLogin(String code) {
    MyPageResponseDTO.LoginResultDTO result = mypageService.googleLogin(code);
    return ApiResponse.onSuccess(
        new LoginResultDTO(
            1L,
            "qws1566@skuniv.ac.kr",
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd3MxNTY2QHNrdW5pdi5hYy5rciIsImV4cCI6MTY0NzQwNjYwNn0.7",
            "",
            LocalDateTime.parse("2022-02-24T07:00:00.000")));
  }
}
