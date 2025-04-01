package org.example.skuhomepage.domain.calendar.controller;

import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.ScheduleResponseDTO;
import org.example.skuhomepage.domain.calendar.exception.CalendarErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "전체 일정", description = "전체 일정 관련 API")
@RequestMapping("/api/calendars")
public interface ScheduleControllerSpec {
  @Operation(summary = "전체 일정 조회", description = "전체 일정을 조회하는 API")
  @ApiErrorCodeExample(CalendarErrorStatus.class)
  @GetMapping
  ApiResponse<List<ScheduleResponseDTO>> getAllCalendar(
      @Parameter(description = "조회 연도", example = "2025") @RequestParam int year,
      @Parameter(description = "조회 월", example = "3") @RequestParam int month,
      @Parameter(description = "조회 일", example = "31")
          @RequestParam(required = false, defaultValue = "0")
          int day,
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          CustomUserDetails userDetails);
}
