package org.example.skuhomepage.domain.calendar.dto;

import org.example.skuhomepage.domain.calendar.entity.UserSchedule;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserScheduleResponseDTO {

  @Schema(description = "개인 일정 조회 응답 DTO")
  public record UserScheduleDTO(
      @Schema(description = "일정 내용", example = "개인 일정") String title,
      @Schema(description = "일정 시작 시간") DateTimeDTO start,
      @Schema(description = "일정 종료 시간") DateTimeDTO end,
      @Schema(description = "종일 여부", example = "false") Boolean allDay,
      @Schema(description = "일정 색상", example = "#000000") String labelColor) {
    public UserScheduleDTO(UserSchedule entity) {
      this(
          entity.getTitle(),
          DateTimeDTO.of(entity.getStartDateTime()),
          DateTimeDTO.of(entity.getEndDateTime()),
          entity.getIsAllDay(),
          entity.getLabelColor());
    }
  }
}
