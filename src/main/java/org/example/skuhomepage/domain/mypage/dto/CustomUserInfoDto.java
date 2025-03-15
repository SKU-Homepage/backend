package org.example.skuhomepage.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserInfoDto {
  private Long userId;
  private String account;
  private String password;
  @Getter private boolean registered;
}
