package org.example.skuhomepage.domain.timetable.dto;

import java.util.List;

import org.example.skuhomepage.domain.timetable.entity.Subject;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class TimeTableResponseDTO {

  public record TodayTimeTableDTO(
      @Schema(description = "id", example = "1") Long id,
      @Schema(description = "수업 이름", example = "자료구조") String subject,
      @Schema(description = "장소", example = "북악관 608호") String classroom,
      @Schema(description = "수업 요일", example = "월") String day,
      @Schema(description = "수업 시작 시간", example = "10:00") String startTime,
      @Schema(description = "수업 종료 시간", example = "11:30") String endTime) {}

  public record MyTimeTableDTO(
      @ArraySchema(
              schema = @Schema(implementation = MySubjectDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<TimeTableDTO> subjects) {}

  public record TimeTableListDTO(
      @ArraySchema(
              schema = @Schema(implementation = TimeTableDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<TimeTableDTO> timeTables,
      @Schema(description = "마지막 존재 여부", example = "true") boolean hasNext,
      @Schema(description = "다음 페이지", example = "2") int nextPage) {}

  public record AddSubjectDTO(@Schema(description = "과목 id", example = "1") Long subjectId) {}

  public record DeleteSubjectDTO(@Schema(description = "과목 id", example = "1") Long subjectId) {}

  public record TimeTableDTO(
      @Schema(description = "과목 id", example = "1") Long subjectId,
      @Schema(description = "과목 이름", example = "자료구조") String subject,
      @Schema(description = "교수 이름", example = "이지영") String professor,
      @Schema(description = "수업 요일", example = "월") String day,
      @Schema(description = "수업 시작 시간", example = "10:00") String startTime,
      @Schema(description = "수업 종료 시간", example = "11:30") String endTime,
      @Schema(description = "장소", example = "북악관 608호") String classroom,
      @Schema(description = "학점", example = "3") int credit,
      @Schema(description = "학년", example = "1") int grade,
      @Schema(description = "수강 대상", example = "컴퓨터공학과") String target,
      @Schema(description = "구분", example = "전공") String division) {
    public TimeTableDTO(Subject subject) {
      this(
          subject.getId(),
          subject.getSubject(),
          subject.getProfessor(),
          subject.getDay(),
          subject.getStartTime(),
          subject.getEndTime(),
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
      @Schema(description = "수업 요일", example = "월") String day,
      @Schema(description = "수업 시작 시간", example = "10:00") String startTime,
      @Schema(description = "수업 종료 시간", example = "11:30") String endTime,
      @Schema(description = "장소", example = "북악관 608호") String classroom) {}

  public record TodayTimeTableListDTO(
      @ArraySchema(
              schema = @Schema(implementation = TodayTimeTableDTO.class),
              arraySchema = @Schema(description = "과목 리스트"))
          List<TimeTableDTO> timeTables) {}
}
