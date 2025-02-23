package org.example.skuhomepage.domain.calendar.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class SkuCalendarResponseDTO {

  @Builder
  @Schema(description = "학사 일정 조회 응답 DTO")
  public record SkuScheduleDTO(
      @Schema(description = "일정 내용", example = "제 75회 전기 학위수여식") String title,
      @Schema(description = "일정 시작일", example = "2025-02-20") LocalDate startDate,
      @Schema(description = "일정 종료일", example = "2025-02-20") LocalDate endDate) {}

  public static List<SkuScheduleDTO> toSkuScheduleDTOList(GoogleCalendarResponseDTO dto) {

    List<SkuScheduleDTO> schedules = new ArrayList<>();

    for (GoogleCalendarResponseDTO.Item item : dto.getItems()) {
      schedules.add(
          SkuScheduleDTO.builder()
              .title(item.getSummary())
              .startDate(LocalDate.parse(item.getStart().getDate()))
              .endDate(LocalDate.parse(item.getEnd().getDate()))
              .build());
    }

    return schedules;
  }
}
