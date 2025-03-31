package org.example.skuhomepage.domain.calendar.controller;

import java.net.URI;
import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.UserScheduleRequestDTO.*;
import org.example.skuhomepage.domain.calendar.dto.UserScheduleResponseDTO.*;
import org.example.skuhomepage.domain.calendar.service.UserScheduleService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserScheduleController implements UserScheduleControllerSpec {

  private final UserScheduleService userScheduleService;

  @Override
  public ApiResponse<List<UserScheduleDTO>> getUserCalendar(
      int year, int month, int day, CustomUserDetails userDetails) {

    return ApiResponse.onSuccess(
        userScheduleService.getUserSchedule(year, month, day, userDetails.getUserId()));
  }

  @Override
  public ApiResponse<Void> addUserSchedule(
      AddUserScheduleDTO requestDTO, CustomUserDetails userDetails) {

    return ApiResponse.onCreated(
        URI.create(
            "/api/calendars/users/"
                + userScheduleService.addUserSchedule(requestDTO, userDetails.getUserId())));
  }

  @Override
  public ApiResponse<UserScheduleDTO> updateUserSchedule(
      long scheduleId, UpdateUserScheduleDTO requestDTO, CustomUserDetails userDetails) {

    return ApiResponse.onSuccess(
        userScheduleService.updateUserSchedule(scheduleId, requestDTO, userDetails.getUserId()));
  }

  @Override
  public ApiResponse<Void> deleteUserSchedule(long scheduleId, CustomUserDetails userDetails) {

    userScheduleService.deleteUserSchedule(scheduleId, userDetails.getUserId());

    return ApiResponse.onSuccess(null);
  }
}
