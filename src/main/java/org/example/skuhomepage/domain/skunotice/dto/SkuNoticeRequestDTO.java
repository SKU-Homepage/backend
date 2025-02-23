package org.example.skuhomepage.domain.skunotice.dto;

import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public class SkuNoticeRequestDTO {

  @Schema(title = "Notice : 공지사항 키워드 DTO")
  public record KeywordDTO(
      @Size(min = 2, max = 10, message = "공지사항 키워드는 2자 이상 10자 이하로 입력해야 합니다.")
          @Schema(description = "공지사항 키워드", example = "장학")
          String keyword) {}
}
