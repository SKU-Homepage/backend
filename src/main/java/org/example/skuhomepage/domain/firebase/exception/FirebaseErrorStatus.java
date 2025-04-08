package org.example.skuhomepage.domain.firebase.exception;

import org.example.skuhomepage.global.apiPayload.code.BaseErrorCode;
import org.example.skuhomepage.global.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FirebaseErrorStatus implements BaseErrorCode {
  KEYWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "KEYWORD404001", "중복된 키워드입니다"),
  KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "KEYWORD404002", "존재하지 않는 키워드입니다."),
  ALARM_NOT_FOUND(HttpStatus.NOT_FOUND,"ALARM404001","존재하지 않는 알람입니다");

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
