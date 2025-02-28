package org.example.skuhomepage.domain.mypage.converter;

import org.example.skuhomepage.domain.mypage.dto.CustomUserInfoDto;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.global.security.GoogleDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserConverter {
  public static CustomUserInfoDto toCustomUserInfoDto(User user) {
    return CustomUserInfoDto.builder().userId(user.getId()).account(user.getAccount()).build();
  }

  public static User toUser(GoogleDTO.UserInfo googleUser, PasswordEncoder passwordEncoder) {
    return User.builder()
        .account(googleUser.getEmail())
        .major("")
        .name(googleUser.getName())
        .grade("1")
        .studentNumber("20210000")
        .college("")
        .department("")
        .password(passwordEncoder.encode("google"))
        .build();
  }

  public static MyPageResponseDTO.MyPageInfoDTO toMyPageInfoDto(User user) {
    return new MyPageResponseDTO.MyPageInfoDTO(
        user.getName(),
        user.getCollege(),
        user.getDepartment(),
        user.getMajor(),
        user.getStudentNumber(),
        user.getGrade());
  }
}
