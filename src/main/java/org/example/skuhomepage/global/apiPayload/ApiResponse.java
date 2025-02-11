package org.example.skuhomepage.global.apiPayload;

import java.net.URI;

import org.example.skuhomepage.global.apiPayload.code.BaseCode;
import org.example.skuhomepage.global.apiPayload.status.CommonSuccessStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {
  @JsonProperty("isSuccess")
  @Schema(description = "API 요청 성공 여부", example = "true")
  private final Boolean isSuccess;

  @Schema(description = "API 응답 코드", example = "COMMON200")
  private final String code;

  @Schema(description = "API 응답 메시지", example = "응답에 성공했습니다.")
  private final String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "API URI", example = "/api/resource/{id}", hidden = true)
  private URI location;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "API 응답 데이터")
  private T result;

  public static <T> ApiResponse<T> onSuccess(T result) {
    return new ApiResponse<>(
        true, CommonSuccessStatus._OK.getCode(), CommonSuccessStatus._OK.getMessage(), result);
  }

  public static <T> ApiResponse<T> onCreated(T location) {
    return new ApiResponse<>(
        true,
        CommonSuccessStatus._CREATED.getCode(),
        CommonSuccessStatus._CREATED.getReasonHttpStatus().getMessage(),
        location);
  }

  public static <T> ApiResponse<T> of(BaseCode code, T result) {
    return new ApiResponse<>(
        true,
        code.getReasonHttpStatus().getCode(),
        code.getReasonHttpStatus().getMessage(),
        result);
  }

  public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
    return new ApiResponse<>(false, code, message, data);
  }
}

