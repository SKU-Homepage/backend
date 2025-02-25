package org.example.skuhomepage.domain.mypage.converter;

import org.example.skuhomepage.domain.mypage.dto.CustomUserInfoDto;
import org.example.skuhomepage.domain.mypage.entity.CollegeType;
import org.example.skuhomepage.domain.mypage.entity.DepartmentType;
import org.example.skuhomepage.domain.mypage.entity.MajorType;
import org.example.skuhomepage.domain.mypage.entity.StatusType;
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
        .major(MajorType.공통전공)
        .name(googleUser.getName())
        .grade("1")
        .status(StatusType.재학)
        .studentNumber("20210000")
        .college(CollegeType.공동대학)
        .department(DepartmentType.공통학과)
        .password(passwordEncoder.encode("google"))
        .build();
  }
}
