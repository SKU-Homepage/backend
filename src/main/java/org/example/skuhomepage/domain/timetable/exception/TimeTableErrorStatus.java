package org.example.skuhomepage.domain.timetable.exception;

import org.example.skuhomepage.global.apiPayload.code.BaseErrorCode;
import org.example.skuhomepage.global.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeTableErrorStatus implements BaseErrorCode {
  TIME_TABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "TIMETABLE4001", "시간표를 찾을 수 없습니다."),
  DUPLICATE_SUBJECT(HttpStatus.BAD_REQUEST, "TIMETABLE4002", "이미 등록된 과목입니다."),
  SUBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "TIMETABLE4003", "과목을 찾을 수 없습니다."),
  INVALID_SUBJECT_ID(HttpStatus.BAD_REQUEST, "TIMETABLE4004", "과목 ID가 잘못되었습니다."),
  SUBJECT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "TIMETABLE4005", "이미 등록된 과목입니다.");

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
