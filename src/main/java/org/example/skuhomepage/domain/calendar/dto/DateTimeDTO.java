package org.example.skuhomepage.domain.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "날짜, 시간 DTO")
public record DateTimeDTO(
    @Schema(description = "날짜", example = "2025-03-31") LocalDate date,
    @Schema(description = "시간", example = "00:00:00") LocalTime time) {}
