package org.example.skuhomepage.domain.mypage.exception;

import org.example.skuhomepage.global.apiPayload.code.BaseErrorCode;
import org.example.skuhomepage.global.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyPageErrorStatus implements BaseErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "MYPAGE404001", "사용자를 찾을 수 없습니다."),
  EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "MYPAGE400001", "이메일 형식이 skuniv가 아닙니다"),
  TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "MYPAGE400002", "타입이 올바르지 않습니다"),
  TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MYPAGE400003", "타입을 찾을 수 없습니다"),
  DUPLICATE_STUDENT_NUMBER(HttpStatus.BAD_REQUEST, "MYPAGE400004", "중복된 학번입니다"),
  USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "MYPAGE400005", "이미 회원가입을 완료한 사용자입니다"),
  USER_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "MYPAGE400006", "회원가입을 완료하지 않은 사용자입니다");

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
