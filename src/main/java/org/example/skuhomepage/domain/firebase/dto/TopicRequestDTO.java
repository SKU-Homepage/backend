package org.example.skuhomepage.domain.firebase.dto;

import org.example.skuhomepage.global.enums.TopicGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "키워드 요청 DTO")
public class TopicRequestDTO {
  @Schema(description = "키워드", example = "장학금")
  private String keyword;

  @Schema(description = "토픽 그룹", example = "SKU_NOTICE")
  private TopicGroup topicGroup;
}
