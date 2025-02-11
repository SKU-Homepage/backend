package org.example.skuhomepage.domain.calendar.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

public class SkuCalendarResponseDTO {

  @Schema(description = "학사 일정 조회 응답 DTO")
  public record SkuScheduleDTO(
      @Schema(description = "일정 내용", example = "제 75회 전기 학위수여식") String title,
      @Schema(description = "일정 시작일", example = "2025-02-20") LocalDate startDate,
      @Schema(description = "일정 종료일", example = "2025-02-20") LocalDate endDate) {}
}
