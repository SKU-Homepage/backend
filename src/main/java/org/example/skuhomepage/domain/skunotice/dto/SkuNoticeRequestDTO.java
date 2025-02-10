package org.example.skuhomepage.domain.skunotice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SkuNoticeRequestDTO {

  @Schema(title = "Notice : 공지사항 키워드 DTO")
  public record KeywordDTO(@Schema(description = "공지사항 키워드", example = "장학") String keyword) {}
}
