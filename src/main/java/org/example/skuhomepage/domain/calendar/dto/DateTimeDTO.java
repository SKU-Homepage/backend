package org.example.skuhomepage.domain.calendar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "날짜, 시간 DTO")
public record DateTimeDTO(
    @Schema(description = "날짜", example = "2025-03-31") LocalDate date,
    @Schema(description = "시간", example = "00:00:00") LocalTime time) {

  @JsonIgnore
  public boolean isValidDate() {
    return this.date() == null || this.time() == null;
  }

  @JsonIgnore
  public boolean isAfter(DateTimeDTO dateTimeDTO) {
    return this.date().isAfter(dateTimeDTO.date()) || this.time().isAfter(dateTimeDTO.time());
  }

  @JsonIgnore
  public boolean isBefore(DateTimeDTO dateTimeDTO) {
    return this.date().isBefore(dateTimeDTO.date()) || this.time().isBefore(dateTimeDTO.time());
  }

  public static DateTimeDTO of(LocalDateTime dateTime) {
    return new DateTimeDTO(dateTime.toLocalDate(), dateTime.toLocalTime());
  }
}
