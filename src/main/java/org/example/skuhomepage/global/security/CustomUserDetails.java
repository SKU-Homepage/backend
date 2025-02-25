package org.example.skuhomepage.global.security;

import java.util.Collection;
import java.util.Collections;

import org.example.skuhomepage.domain.mypage.dto.CustomUserInfoDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
  private final CustomUserInfoDto user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // 권한 부여
  }

  @Override
  public String getPassword() {
    return user.getPassword(); // 인코딩된 비밀번호 반환
  }

  @Override
  public String getUsername() {
    return user.getAccount(); // 계정 이름이나 이메일 반환
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Long getUserId() {
    return user.getUserId();
  }
}
