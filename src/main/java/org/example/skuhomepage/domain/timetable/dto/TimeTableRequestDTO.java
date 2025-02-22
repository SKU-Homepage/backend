package org.example.skuhomepage.domain.timetable.dto;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class TimeTableRequestDTO {
  @Getter
  public static class selfSubjectDTO {
    @NotBlank
    @Schema(description = "과목 이름", example = "자료구조")
    private String subject;

    @NotBlank
    @Schema(description = "교수 이름", example = "이지영")
    private String professor;

    @NotBlank
    @Schema(description = "수업 시간", example = "월 1교시")
    private String time;

    @NotBlank
    @Schema(description = "장소", example = "북악관 608호")
    private String classroom;
  }
}
