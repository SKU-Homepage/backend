package org.example.skuhomepage.domain.firebase.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.example.skuhomepage.domain.firebase.entity.UserKeyword;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class NotificationResponseDTO {
  public record NotificationDTO(
      @Schema(description = "알림 id", example = "1") Long id,
      @Schema(description = "제목", example = "2024 콘텐츠 인사이트: 상상은 현실이 된다") String title,
      @Schema(description = "종류", example = "/notice") String type,
      @Schema(description = "전송시간", example = "2025-04-07") LocalDateTime time,
      @Schema(description = "읽음 여부", example = "false") boolean isRead) {}

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

  public record alarmDTO(@Schema(description = "리다이렉트 uri", example = "/notice") String url) {}
}
