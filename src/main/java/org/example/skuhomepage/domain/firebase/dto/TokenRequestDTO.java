package org.example.skuhomepage.domain.firebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "토큰 요청 DTO")
public class TokenRequestDTO {
  @Schema(description = "토큰", example = "fcm_token")
  private String token;
}
