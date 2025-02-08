package org.example.skuhomepage.domain.timetable.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record TimeTableResponseDTO() {

  public record TodayTimeTableDTO(
      @Schema(description = "수업 시간", example = "1교시") // 크롤링 값에 따라 example 변경 될 수도 있음
          String time,
      @Schema(description = "수업 이름", example = "자료구조") String subject,
      @Schema(description = "장소", example = "북악관 608호") String classroom) {}

  public record MyTimeTableDTO(
      @ArraySchema(
              schema = @Schema(implementation = MySubjectDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<MySubjectDTO> subjects) {}

  public record TimeTableListDTO(
      @ArraySchema(
              schema = @Schema(implementation = TimeTableDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<TimeTableDTO> timeTables) {}

  public record AddSubjectDTO(@Schema(description = "과목 id", example = "1") Long subjectId) {}

  public record DeleteSubjectDTO(@Schema(description = "과목 id", example = "1") Long subjectId) {}

  public record TimeTableDTO(
      @Schema(description = "과목 id", example = "1") Long subjectId,
      @Schema(description = "과목 이름", example = "자료구조") String subject,
      @Schema(description = "교수 이름", example = "이지영") String professor,
      @Schema(description = "수업 시간", example = "월 1교시") String time,
      @Schema(description = "장소", example = "북악관 608호") String classroom,
      @Schema(description = "학점", example = "3") int credit,
      @Schema(description = "학년", example = "1") int grade,
      @Schema(description = "수강 대상", example = "컴퓨터공학과") String target,
      @Schema(description = "구분", example = "전공") String division) {}

  public record MySubjectDTO(
      @Schema(description = "과목 id", example = "1") Long subjectId,
      @Schema(description = "과목 이름", example = "자료구조") String subject,
      @Schema(description = "수업 시간", example = "월 1교시") String time,
      @Schema(description = "장소", example = "북악관 608호") String classroom) {}
}
