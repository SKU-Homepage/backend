package org.example.skuhomepage.domain.calendar.controller;

import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.SkuCalendarResponseDTO.*;
import org.example.skuhomepage.domain.calendar.service.SkuCalendarService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SkuCalendarController implements SkuCalendarControllerSpec {

  private final SkuCalendarService skuCalendarService;

  @Override
  public ApiResponse<List<SkuScheduleDTO>> getSkuCalendar(int year, int month, int day) {

    return ApiResponse.onSuccess(skuCalendarService.getSkuCalendar(year, month, day));
  }
}
