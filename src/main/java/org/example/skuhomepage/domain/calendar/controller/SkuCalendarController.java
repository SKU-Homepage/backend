package org.example.skuhomepage.domain.calendar.controller;

import java.time.LocalDate;
import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.SkuCalendarResponseDTO.*;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SkuCalendarController implements SkuCalendarControllerSpec {

  @Override
  public ApiResponse<List<SkuScheduleDTO>> getSkuCalendar(int year, int month, int day) {

    return ApiResponse.onSuccess(
        List.of(
            new SkuScheduleDTO(
                "제 75회 전기 학위수여식", LocalDate.of(2025, 2, 20), LocalDate.of(2025, 2, 20))));
  }
}
