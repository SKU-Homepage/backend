package org.example.skuhomepage.domain.calendar.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.GoogleCalendarResponseDTO;
import org.example.skuhomepage.domain.calendar.dto.SkuCalendarResponseDTO;
import org.example.skuhomepage.domain.calendar.dto.SkuCalendarResponseDTO.SkuScheduleDTO;
import org.example.skuhomepage.domain.calendar.exception.CalendarErrorStatus;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuCalendarService {

  private final RestTemplate restTemplate;

  @Value("${sku.calendar.url}")
  String apiUrl;

  @Value("${sku.calendar.key}")
  String apiKey;

  public List<SkuScheduleDTO> getSkuCalendar(int year, int month, int day) {

    LocalDateTime timeMin =
        (day != 0)
            ? LocalDateTime.of(year, month, day, 0, 0, 0, 0)
            : LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
    LocalDateTime timeMax =
        (day != 0)
            ? LocalDateTime.of(year, month, day, 23, 59, 59, 999999999)
            : YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59, 999999999);

    return getScheduleList(timeMin, timeMax);
  }

  private List<SkuScheduleDTO> getScheduleList(LocalDateTime timeMin, LocalDateTime timeMax) {

    String singleEvents = "true";
    String maxResults = "99";
    String timeZone = "Asia/Seoul";

    String url =
        UriComponentsBuilder.fromUriString(apiUrl)
            .queryParam("key", apiKey)
            .queryParam(
                "timeMin",
                URLEncoder.encode(
                    timeMin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")),
                    StandardCharsets.UTF_8))
            .queryParam(
                "timeMax",
                URLEncoder.encode(
                    timeMax.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")),
                    StandardCharsets.UTF_8))
            .queryParam("singleEvents", singleEvents)
            .queryParam("maxResults", maxResults)
            .queryParam("timeZone", URLEncoder.encode(timeZone, StandardCharsets.UTF_8))
            .build(true)
            .toUriString();

    log.info("[GET] 구글 캘린더 API url: {}", url);

    ResponseEntity<GoogleCalendarResponseDTO> response =
        restTemplate.exchange(
            URI.create(url),
            HttpMethod.GET,
            new HttpEntity<>(null),
            GoogleCalendarResponseDTO.class);

    if (response.getStatusCode().isError()) {
      log.warn("구글 캘린더 API 호출 중 에러 발생: {}", response.getBody());
      throw new GeneralException(CalendarErrorStatus.SKU_CALENDAR_ERROR);
    }

    return SkuCalendarResponseDTO.toSkuScheduleDTOList(response.getBody());
  }
}
