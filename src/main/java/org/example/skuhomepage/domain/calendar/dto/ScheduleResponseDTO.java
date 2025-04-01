package org.example.skuhomepage.domain.calendar.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScheduleResponseDTO(
    @Schema(description = "일정 ID", example = "일정 ID") String id,
    @Schema(description = "일정 내용", example = "일정") String title,
    @Schema(description = "일정 시작 시간") DateTimeDTO start,
    @Schema(description = "일정 종료 시간") DateTimeDTO end,
    @Schema(description = "종일 여부", example = "false") Boolean allDay,
    @Schema(description = "일정 색상", example = "#000000") String labelColor) {

  // 기본 생성자
  public ScheduleResponseDTO(
      String id,
      String title,
      DateTimeDTO start,
      DateTimeDTO end,
      Boolean allDay,
      String labelColor) {
    this.id = id;
    this.title = title;
    this.start = start;
    this.end = end;
    this.allDay = allDay;
    this.labelColor = labelColor;
  }

  // 유저 스케쥴 to 스케쥴
  public ScheduleResponseDTO(UserScheduleResponseDTO.UserScheduleDTO dto) {
    this(
        String.valueOf(dto.id()),
        dto.title(),
        dto.start(),
        dto.end(),
        dto.allDay(),
        dto.labelColor());
  }

  // Sku 스케쥴 to 스케쥴
  public ScheduleResponseDTO(SkuCalendarResponseDTO.SkuScheduleDTO dto) {
    this(
        dto.id(),
        dto.title(),
        DateTimeDTO.of(dto.startDate().atStartOfDay()),
        DateTimeDTO.of(dto.endDate().plusDays(1).atStartOfDay().minusNanos(1)),
        true,
        null);
  }

  public static List<ScheduleResponseDTO> toDtoList(
      List<UserScheduleResponseDTO.UserScheduleDTO> userScheduleDTOS,
      List<SkuCalendarResponseDTO.SkuScheduleDTO> skuScheduleDTOS) {

    List<ScheduleResponseDTO> scheduleResponseDTOS = new ArrayList<>();

    userScheduleDTOS.forEach(
        userScheduleDTO -> {
          scheduleResponseDTOS.add(new ScheduleResponseDTO(userScheduleDTO));
        });
    skuScheduleDTOS.forEach(
        skuScheduleDTO -> {
          scheduleResponseDTOS.add(new ScheduleResponseDTO(skuScheduleDTO));
        });

    scheduleResponseDTOS.sort(
        (o1, o2) -> {
          if (o1.start().isBefore(o2.start())) {
            return -1;
          } else if (o1.start().isAfter(o2.start())) {
            return 1;
          } else {
            return 0;
          }
        });

    return scheduleResponseDTOS;
  }
}
