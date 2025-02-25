package org.example.skuhomepage.domain.mypage.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public class MyPageRequestDTO {
  public record signupRequestDTO(
      @Schema(description = "이름", example = "김서경") String name,
      @Schema(description = "단과대학", example = "예술대학") String college,
      @Schema(description = "학부", example = "디자인학부") String department,
      @Schema(description = "전공", example = "전공") String major,
      @Schema(description = "학번", example = "2021418008") String studentNumber,
      @Schema(description = "학년", example = "4학년") String grade,
      @Schema(description = "재학상태", example = "재학") String status,
      @Schema(description = "약관 동의 여부 1", example = "true") boolean agreement1,
      @Schema(description = "약관 동의 여부 2", example = "true") boolean agreement2) {}
}

