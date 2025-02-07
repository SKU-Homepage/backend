package org.example.skuhomepage.global.apiPayload.code;

public interface BaseErrorCode {
  ErrorReasonDTO getReason();

  ErrorReasonDTO getReasonHttpStatus();
}
