package org.example.skuhomepage.domain.calendar.controller;

import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.UserCalendarRequestDTO.*;
import org.example.skuhomepage.domain.calendar.dto.UserCalendarResponseDTO.*;
import org.example.skuhomepage.domain.calendar.exception.CalendarErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "개인 일정", description = "개인 일정 관련 API")
@RequestMapping("/api/calendars/users")
public interface UserCalendarControllerSpec {

  @Operation(summary = "개인 일정 조회", description = "개인 일정을 조회하는 API")
  @ApiErrorCodeExample(CalendarErrorStatus.class)
  @GetMapping
  ApiResponse<List<UserScheduleDTO>> getUserCalendar(
      @Parameter(description = "조회 연도", example = "2025") @RequestParam int year,
      @Parameter(description = "조회 월", example = "3") @RequestParam int month,
      @Parameter(description = "조회 일", example = "31") @RequestParam(required = false) int day);

  @Operation(summary = "개인 일정 추가", description = "개인 일정을 추가하는 API")
  @ApiErrorCodeExample(CalendarErrorStatus.class)
  @PostMapping
  ApiResponse<UserScheduleCreateDTO> addUserSchedule(@RequestBody AddUserScheduleDTO requestDTO);

  @Operation(summary = "개인 일정 수정", description = "개인 일정을 수정하는 API")
  @ApiErrorCodeExample(CalendarErrorStatus.class)
  @PatchMapping("/{scheduleId}")
  ApiResponse<UserScheduleDTO> updateUserSchedule(
      @Parameter(description = "일정 ID", example = "1") @PathVariable long scheduleId,
      @RequestBody UpdateUserScheduleDTO requestDTO);

  @Operation(summary = "개인 일정 삭제", description = "개인 일정을 삭제하는 API")
  @ApiErrorCodeExample(CalendarErrorStatus.class)
  @DeleteMapping("/{scheduleId}")
  ApiResponse<Void> deleteUserSchedule(
      @Parameter(description = "일정 ID", example = "1") @PathVariable long scheduleId);
}
