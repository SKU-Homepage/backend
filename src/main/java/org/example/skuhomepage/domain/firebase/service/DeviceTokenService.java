package org.example.skuhomepage.domain.firebase.service;

import org.example.skuhomepage.domain.firebase.dto.TokenRequestDTO;
import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.firebase.repository.UserDeviceTokenRepository;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

  private final UserRepository userRepository;
  private final UserDeviceTokenRepository tokenRepository;

  public void registerToken(TokenRequestDTO request, UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    tokenRepository
        .findByFcmToken(request.getFcmToken())
        .ifPresentOrElse(
            existingToken -> {
              // 이미 등록된 경우
              if (!existingToken.getUser().getId().equals(user.getId())) {
                existingToken =
                    UserDeviceToken.builder()
                        .id(existingToken.getId())
                        .fcmToken(existingToken.getFcmToken())
                        .user(user)
                        .build();
                tokenRepository.save(existingToken);
              }
            },
            () -> {
              // 새로 등록
              UserDeviceToken token =
                  UserDeviceToken.builder().user(user).fcmToken(request.getFcmToken()).build();

              tokenRepository.save(token);
            });
  }
}
