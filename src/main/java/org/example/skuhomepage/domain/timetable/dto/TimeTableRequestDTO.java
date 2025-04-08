package org.example.skuhomepage.domain.timetable.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TimeTableRequestDTO {
  @Getter
  public static class selfSubjectDTO {
    @NotBlank
    @Schema(description = "과목 이름", example = "자료구조")
    private String subject;

    @NotBlank
    @Schema(description = "수업 요일", example = "월")
    private String day;

    @NotBlank
    @Schema(description = "수업 시작 시간", example = "9:00")
    private String startTime;

    @NotBlank
    @Schema(description = "수업 종료 시간", example = "10:15")
    private String endTime;

    @NotBlank
    @Schema(description = "장소", example = "북악관 608호")
    private String classroom;
  }

  @Getter
  @AllArgsConstructor
  public static class AddSubjectDTO {
    private List<Long> subjectIds;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  public static class TimeTableSubjectDTO {
    private String subjectName;
    private String day;
    private String startTime;
    private String endTime;
  }
}
