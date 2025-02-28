package org.example.skuhomepage.domain.mypage.dto;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MyPageRequestDTO {
  @Getter
  @Setter
  @NoArgsConstructor
  public static class signupRequestDTO {

    @Schema(description = "단과대학", example = "예술대학")
    @NotNull
    private String college;

    @Schema(description = "학부", example = "디자인학부")
    @NotNull
    private String department;

    @Schema(description = "전공", example = "전공")
    @NotNull
    private String major;

    @Schema(description = "학번", example = "2021418008")
    @NotNull
    private String studentNumber;

    @Schema(description = "학년", example = "4학년")
    @NotNull
    private String grade;

    @Schema(description = "약관 동의 여부", example = "true")
    @NotNull
    private boolean agreement;
  }
}
