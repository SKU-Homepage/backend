package org.example.skuhomepage.domain.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserCalendarResponseDTO {

  @Schema(description = "개인 일정 조회 응답 DTO")
  public record UserScheduleDTO(
      @Schema(description = "일정 내용", example = "개인 일정") String title,
      @Schema(description = "일정 시작 시간") DateTimeDTO start,
      @Schema(description = "일정 종료 시간") DateTimeDTO end,
      @Schema(description = "종일 여부", example = "false") boolean allDay) {}
}
