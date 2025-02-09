package org.example.skuhomepage.domain.calendar.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.DateTimeDTO;
import org.example.skuhomepage.domain.calendar.dto.UserCalendarRequestDTO.*;
import org.example.skuhomepage.domain.calendar.dto.UserCalendarResponseDTO.*;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserCalendarController implements UserCalendarControllerSpec {

  @Override
  public ApiResponse<List<UserScheduleDTO>> getUserCalendar(int year, int month, int day) {

    return ApiResponse.onSuccess(
        List.of(
            new UserScheduleDTO(
                "개인 일정",
                new DateTimeDTO(LocalDate.of(2025, 3, 31), LocalTime.of(0, 0, 0)),
                new DateTimeDTO(LocalDate.of(2025, 3, 31), LocalTime.of(23, 59, 59)),
                false)));
  }

  @Override
  public ApiResponse<Void> addUserSchedule(AddUserScheduleDTO requestDTO) {

    return ApiResponse.onCreated(URI.create("/api/calendars/users/1"));
  }

  @Override
  public ApiResponse<UserScheduleDTO> updateUserSchedule(
      long scheduleId, UpdateUserScheduleDTO requestDTO) {

    return ApiResponse.onSuccess(
        new UserScheduleDTO(
            requestDTO.getTitle() == null ? "개인 일정" : requestDTO.getTitle(),
            requestDTO.getStart() == null
                ? new DateTimeDTO(LocalDate.of(2025, 3, 31), LocalTime.of(0, 0, 0))
                : requestDTO.getStart(),
            requestDTO.getEnd() == null
                ? new DateTimeDTO(LocalDate.of(2025, 3, 31), LocalTime.of(23, 59, 59))
                : requestDTO.getEnd(),
            false));
  }

  @Override
  public ApiResponse<Void> deleteUserSchedule(long scheduleId) {

    return ApiResponse.onSuccess(null);
  }
}
