package org.example.skuhomepage.domain.calendar.controller;

import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.SkuCalendarResponseDTO;
import org.example.skuhomepage.domain.calendar.exception.CalendarErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "학사 일정", description = "학사 일정 관련 API")
@RequestMapping("/api/calendars/sku")
public interface SkuCalendarControllerSpec {

  @Operation(summary = "학사 일정 조회", description = "학사 일정을 조회하는 API")
  @ApiErrorCodeExample(CalendarErrorStatus.class)
  @GetMapping
  ApiResponse<List<SkuCalendarResponseDTO.SkuScheduleDTO>> getSkuCalendar(
      @Parameter(description = "조회 연도", example = "2025") @RequestParam int year,
      @Parameter(description = "조회 월", example = "2") @RequestParam int month,
      @Parameter(description = "조회 일", example = "20")
          @RequestParam(required = false, defaultValue = "0")
          int day);
}
