package org.example.skuhomepage.domain.mypage.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class MyPageResponseDTO {
  public record MyPageInfoDTO(
      @Schema(description = "이름", example = "김서경") String name,
      @Schema(description = "단과대학", example = "예술대학") String college,
      @Schema(description = "학부", example = "디자인학부") String department,
      @Schema(description = "전공", example = "전공") String major,
      @Schema(description = "학번", example = "2021418008") String studentNumber,
      @Schema(description = "학년", example = "4학년") String grade,
      @Schema(description = "재학상태", example = "재학") String status) {}

  public record LoginResultDTO(
      @Schema(description = "memberId", example = "1") Long memberId,
      @Schema(description = "account", example = "qws1566@skuniv.ac.kr") String account,
      @Schema(
              description = "accessToken",
              example =
                  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd3MxNTY2QHNrdW5pdi5hYy5rciIsImV4cCI6MTY0NzQwNjYwNn0.7")
          String accessToken,
      @Schema(description = "refreshToken", example = "") String refreshToken,
      @Schema(description = " createdAt", example = "2022-02-24T07:00:00.000+00:00")
          LocalDateTime createdAt) {}

  public record SignupResultDTO(
      @Schema(description = "이름", example = "김서경") String name,
      @Schema(description = "단과대학", example = "예술대학") String college,
      @Schema(description = "학부", example = "디자인학부") String department,
      @Schema(description = "전공", example = "전공") String major,
      @Schema(description = "학번", example = "2021418008") String studentNumber,
      @Schema(description = "학년", example = "4학년") String grade,
      @Schema(description = "재학상태", example = "재학") String status) {}
}
