package org.example.skuhomepage.domain.mypage.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.example.skuhomepage.domain.mypage.converter.UserConverter;
import org.example.skuhomepage.domain.mypage.dto.MyPageRequestDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.LoginResultDTO;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO.signUpResultDTO;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.example.skuhomepage.global.security.GoogleDTO;
import org.example.skuhomepage.global.security.GoogleUtil;
import org.example.skuhomepage.global.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
  private final GoogleUtil googleUtil;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public MyPageResponseDTO.MyPageInfoDTO getMyInfo(UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));
    return UserConverter.toMyPageInfoDto(user);
  }

  public signUpResultDTO signUp(
      UserDetails userDetails, MyPageRequestDTO.signupRequestDTO request) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));
    user.updateUserInfo(
        request.getCollege(),
        request.getDepartment(),
        request.getMajor(),
        request.getStudentNumber(),
        request.getGrade(),
        request.isAgreement());
    userRepository.save(user);
    return new MyPageResponseDTO.signUpResultDTO(user.getId());
  }

  public MyPageResponseDTO.LoginResultDTO googleLogin(String code, int env) {
    GoogleDTO.OAuthToken oAuthToken = googleUtil.requestToken(code, env);
    GoogleDTO.UserInfo userInfo = googleUtil.requestUserInfo(oAuthToken.getAccess_token());

    validateSkunivEmail(userInfo.getEmail());

    User user = findOrCreateUser(userInfo);
    String token = jwtUtil.createAccessToken(UserConverter.toCustomUserInfoDto(user));

    return LoginResultDTO.from(user, token);
  }

  private void validateSkunivEmail(String email) {
    if (!email.endsWith("@skuniv.ac.kr")) {
      throw new GeneralException(MyPageErrorStatus.EMAIL_NOT_VALID);
    }
  }

  private User findOrCreateUser(GoogleDTO.UserInfo userInfo) {
    return userRepository
        .findByAccount(userInfo.getEmail())
        .orElseGet(() -> createNewUser(userInfo));
  }

  private User createNewUser(GoogleDTO.UserInfo googleUser) {
    User newUser = UserConverter.toUser(googleUser, passwordEncoder);
    return userRepository.save(newUser);
  }
}
