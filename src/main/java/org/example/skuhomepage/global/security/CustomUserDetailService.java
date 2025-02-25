package org.example.skuhomepage.global.security;

import org.example.skuhomepage.domain.mypage.dto.CustomUserInfoDto;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByAccount(account)
            .orElseThrow(
                () -> new UsernameNotFoundException("해당 이메일을 가진 유저가 존재하지 않습니다: " + account));

    CustomUserInfoDto userInfoDto =
        CustomUserInfoDto.builder()
            .userId(user.getId())
            .password(user.getPassword())
            .account(user.getAccount())
            .build();

    return new CustomUserDetails(userInfoDto);
  }
}
