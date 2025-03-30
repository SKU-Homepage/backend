package org.example.skuhomepage.domain.firebase.dto;

import java.util.List;

import org.example.skuhomepage.domain.firebase.entity.UserKeyword;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class NotificationResponseDTO {
  public record NotificationDTO(
      @Schema(description = "제목", example = "2024 콘텐츠 인사이트: 상상은 현실이 된다") String title,
      @Schema(description = "종류", example = "공지사항") String name) {}

  public record NotificationListDTO(
      @ArraySchema(
              schema = @Schema(implementation = NotificationResponseDTO.NotificationDTO.class),
              arraySchema = @Schema(description = "알림 리스트"))
          List<NotificationResponseDTO.NotificationDTO> notificationDTOS) {}

  public record keywordDTO(
      @ArraySchema(
              schema = @Schema(implementation = UserKeyword.class),
              arraySchema = @Schema(description = "키워드 리스트"))
          List<UserKeywordDTO> userKeywordList) {}

  public record UserKeywordDTO(@Schema(description = "키워드", example = "대학혁신") String name) {}
}
