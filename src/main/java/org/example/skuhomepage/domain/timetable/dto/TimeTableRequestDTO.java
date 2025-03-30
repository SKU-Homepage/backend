package org.example.skuhomepage.domain.timetable.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class TimeTableRequestDTO {
  @Getter
  public static class selfSubjectDTO {
    @NotBlank
    @Schema(description = "과목 이름", example = "자료구조")
    private String subject;

    @NotBlank
    @Schema(description = "수업 시간", example = "월 1교시")
    private String time;

    @NotBlank
    @Schema(description = "장소", example = "북악관 608호")
    private String classroom;
  }

  @Getter
  @AllArgsConstructor
  public static class AddSubjectDTO {
    private List<Long> subjectIds;
  }
}
