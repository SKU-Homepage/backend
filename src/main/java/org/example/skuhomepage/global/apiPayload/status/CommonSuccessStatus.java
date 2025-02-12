package org.example.skuhomepage.global.apiPayload.status;

import org.example.skuhomepage.global.apiPayload.code.BaseCode;
import org.example.skuhomepage.global.apiPayload.code.ReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonSuccessStatus implements BaseCode {
  // Common Success

  _OK(HttpStatus.OK, "COMMON200", "응답에 성공했습니다."),
  _CREATED(HttpStatus.CREATED, "COMMON201", "리소스가 성공적으로 생성되었습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ReasonDTO getReason() {
    return ReasonDTO.builder().message(message).code(code).isSuccess(true).build();
  }

  @Override
  public ReasonDTO getReasonHttpStatus() {
    return ReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(true)
        .httpStatus(httpStatus)
        .build();
  }
}
