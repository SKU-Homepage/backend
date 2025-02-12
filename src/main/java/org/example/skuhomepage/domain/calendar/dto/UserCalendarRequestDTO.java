package org.example.skuhomepage.domain.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class UserCalendarRequestDTO {

  @Getter
  @Schema(description = "개인 일정 생성 요청 DTO")
  public static class AddUserScheduleDTO {

    @NotNull
    @NotBlank
    @Schema(description = "개인 일정 내용", example = "개인 일정")
    private String title;

    @NotNull
    @Schema(description = "시작 시간")
    private DateTimeDTO start;

    @NotNull
    @Schema(description = "종료 시간")
    private DateTimeDTO end;

    @Schema(description = "종일 여부", example = "false")
    private boolean allDay;
  }

  @Getter
  @Schema(description = "개인 일정 수정 요청 DTO")
  public static class UpdateUserScheduleDTO {

    @Schema(description = "개인 일정 내용", example = "개인 일정")
    private String title;

    @Schema(description = "시작 시간")
    private DateTimeDTO start;

    @Schema(description = "종료 시간")
    private DateTimeDTO end;

    @Schema(description = "종일 여부", example = "false")
    private boolean allDay;
  }
}
