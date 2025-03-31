package org.example.skuhomepage.domain.calendar.controller;

import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.ScheduleResponseDTO;
import org.example.skuhomepage.domain.calendar.service.UserScheduleService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ScheduleController implements ScheduleControllerSpec {

  private final UserScheduleService userScheduleService;

  @Override
  public ApiResponse<List<ScheduleResponseDTO>> getAllCalendar(
      int year, int month, int day, CustomUserDetails userDetails) {

    return ApiResponse.onSuccess(
        userScheduleService.getAllSchedule(year, month, day, userDetails.getUserId()));
  }
}
