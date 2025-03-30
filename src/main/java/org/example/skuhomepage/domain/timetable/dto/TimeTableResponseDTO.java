package org.example.skuhomepage.domain.timetable.dto;

import java.util.List;

import org.example.skuhomepage.domain.timetable.entity.Subject;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class TimeTableResponseDTO {

  public record TodayTimeTableDTO(
      @Schema(description = "id", example = "1") Long id,
      @Schema(description = "수업 시간", example = "1교시") // 크롤링 값에 따라 example 변경 될 수도 있음
          String time,
      @Schema(description = "수업 이름", example = "자료구조") String subject,
      @Schema(description = "장소", example = "북악관 608호") String classroom,
      @Schema(description = "수업 시간", example = "9:00 -11:45") String classtime) {}

  public record MyTimeTableDTO(
      @ArraySchema(
              schema = @Schema(implementation = MySubjectDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<MySubjectDTO> subjects) {}

  public record TimeTableListDTO(
      @ArraySchema(
              schema = @Schema(implementation = TimeTableDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<TimeTableDTO> timeTables,
      @Schema(description = "마지막 존재 여부", example = "true") boolean hasNext,
      @Schema(description = "다음 페이지", example = "2") int nextPage) {}

  public record AddSubjectDTO(
      @Schema(description = "과목 id", example = "1") List<Long> subjectIds) {}

  public record SelfSubjectDTO(@Schema(description = "과목 id", example = "1") Long subjectId) {}

  public record DeleteSubjectDTO(@Schema(description = "과목 id", example = "1") Long subjectId) {}

  public record TimeTableDTO(
      @Schema(description = "과목 id", example = "1") Long subjectId,
      @Schema(description = "과목 이름", example = "자료구조") String subject,
      @Schema(description = "교수 이름", example = "이지영") String professor,
      @Schema(description = "수업 시간", example = "월 1교시") String time,
      @Schema(description = "장소", example = "북악관 608호") String classroom,
      @Schema(description = "학점", example = "3") String credit,
      @Schema(description = "학년", example = "1") String grade,
      @Schema(description = "수강 대상", example = "컴퓨터공학과") String target,
      @Schema(description = "구분", example = "전공") String division) {
    public TimeTableDTO(Subject subject) {
      this(
          subject.getId(),
          subject.getSubject(),
          subject.getProfessor(),
          subject.getTime(),
          subject.getClassroom(),
          subject.getCredit(),
          subject.getGrade(),
          subject.getTarget(),
          subject.getDivision() != null ? subject.getDivision().name() : null);
    }
  }

  public record MySubjectDTO(
      @Schema(description = "과목 id", example = "1") Long subjectId,
      @Schema(description = "과목 이름", example = "자료구조") String subject,
      @Schema(description = "수업 시간", example = "월 1교시") String time,
      @Schema(description = "장소", example = "북악관 608호") String classroom) {}

  public record TodayTimeTableListDTO(
      @ArraySchema(
              schema = @Schema(implementation = TodayTimeTableDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<TodayTimeTableDTO> timeTables) {}
}
