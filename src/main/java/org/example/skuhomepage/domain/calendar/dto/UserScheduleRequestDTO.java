package org.example.skuhomepage.domain.calendar.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.example.skuhomepage.domain.calendar.entity.UserSchedule;
import org.example.skuhomepage.domain.mypage.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class UserScheduleRequestDTO {

  @Getter
  @Setter
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

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @Schema(description = "일정 색상", example = "#000000")
    private String labelColor;

    public static AddUserScheduleDTO toDto(UserSchedule entity) {

      AddUserScheduleDTO dto = new AddUserScheduleDTO();
      dto.title = entity.getTitle();
      dto.start = DateTimeDTO.of(entity.getStartDateTime());
      dto.end = DateTimeDTO.of(entity.getEndDateTime());
      dto.allDay = entity.getIsAllDay();
      dto.labelColor = entity.getLabelColor();

      return dto;
    }

    public static UserSchedule toEntity(AddUserScheduleDTO dto, User user) {
      return UserSchedule.builder()
          .title(dto.title)
          .startDateTime(LocalDateTime.of(dto.start.date(), dto.start.time()))
          .endDateTime(LocalDateTime.of(dto.end.date(), dto.end.time()))
          .isAllDay(dto.allDay)
          .labelColor(dto.labelColor)
          .user(user)
          .build();
    }
  }

  @Getter
  @Setter
  @Schema(description = "개인 일정 수정 요청 DTO")
  public static class UpdateUserScheduleDTO {

    @Schema(description = "개인 일정 내용", example = "개인 일정")
    private String title;

    @Schema(description = "시작 시간")
    private DateTimeDTO start;

    @Schema(description = "종료 시간")
    private DateTimeDTO end;

    @Schema(description = "종일 여부", example = "false")
    private Boolean allDay;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @Schema(description = "일정 색상", example = "#000000")
    private String labelColor;
  }
}
