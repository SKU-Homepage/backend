package org.example.skuhomepage.domain.mypage.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import jakarta.servlet.http.HttpServletResponse;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

  @Value("${cookie.secure}")
  private boolean isSecure;

  public MyPageResponseDTO.MyPageInfoDTO getMyInfo(UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));
    if (!user.isRegistered()) {
      throw new GeneralException(MyPageErrorStatus.USER_NOT_REGISTERED);
    }
    return UserConverter.toMyPageInfoDto(user);
  }

  public signUpResultDTO signUp(
      UserDetails userDetails, MyPageRequestDTO.signupRequestDTO request) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));
    if (user.isRegistered()) {
      throw new GeneralException(MyPageErrorStatus.USER_ALREADY_REGISTERED);
    }

    if (userRepository.existsByStudentNumber(request.getStudentNumber())) {
      throw new GeneralException(MyPageErrorStatus.DUPLICATE_STUDENT_NUMBER);
    }
    user.updateUserInfo(
        request.getCollege(),
        request.getDepartment(),
        request.getMajor(),
        request.getStudentNumber(),
        request.getGrade(),
        request.isAgreement(),
        true);

    userRepository.save(user);
    return new MyPageResponseDTO.signUpResultDTO(user.getId());
  }

  public MyPageResponseDTO.LoginResultDTO googleLogin(
      String code, String env, HttpServletResponse response) {
    String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
    GoogleDTO.OAuthToken oAuthToken = googleUtil.requestToken(decode, env);
    GoogleDTO.UserInfo userInfo = googleUtil.requestUserInfo(oAuthToken.getAccess_token());

    validateSkunivEmail(userInfo.getEmail());

    User user = findOrCreateUser(userInfo);
    String token = jwtUtil.createAccessToken(UserConverter.toCustomUserInfoDto(user));

    ResponseCookie cookie =
        ResponseCookie.from("token", token)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofDays(7))
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    return LoginResultDTO.from(user, token);
  }

  public MyPageResponseDTO.RegisterDTO isRegistered(UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));
    return MyPageResponseDTO.RegisterDTO.from(user);
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
