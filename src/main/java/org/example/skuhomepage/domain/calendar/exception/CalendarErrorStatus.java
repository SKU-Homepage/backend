package org.example.skuhomepage.domain.calendar.exception;

import org.example.skuhomepage.global.apiPayload.code.BaseErrorCode;
import org.example.skuhomepage.global.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CalendarErrorStatus implements BaseErrorCode {

  // 학사 일정
  SKU_CALENDAR_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CAL500001", "학사 일정 조회 중 오류가 발생했습니다."),

  // 개인 일정
  CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "CAL404001", "캘린더를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ErrorReasonDTO getReason() {
    return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
  }

  @Override
  public ErrorReasonDTO getReasonHttpStatus() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build();
  }
}
