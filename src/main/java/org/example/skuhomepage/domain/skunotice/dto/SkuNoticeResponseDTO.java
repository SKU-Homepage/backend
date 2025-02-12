package org.example.skuhomepage.domain.skunotice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class SkuNoticeResponseDTO {

  @Getter
  @Builder
  @Schema(title = "Notice : 공지사항 DTO")
  public static class NoticeDTO {
    @Schema(description = "공지사항 ID", example = "264265")
    private Long id;

    @Schema(description = "공지사항 제목", example = "2024 콘텐츠 인사이트:상상은 현실이 된다")
    private String title;

    @Schema(description = "공지사항 URL", example = "https://skuNotice.com")
    private String url;

    @Schema(description = "공지사항 발행기관", example = "교수학습원")
    private String department;

    @Schema(description = "공지사항 날짜", example = "2025.01.08")
    private LocalDate date;

    @Schema(description = "공지사항 좋아요", example = "true")
    private boolean like;
  }

  @Getter
  @Builder
  @Schema(title = "Notice : 공지사항 리스트 DTO")
  public static class NoticeListDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "조회 기준 시간", example = "2025-02-12T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "전체 공지사항 리스트")
    List<SkuNoticeResponseDTO.NoticeDTO> noticeList;
  }

  @Getter
  @Builder
  @Schema(title = "Notice : 공지사항 키워드 리스트 DTO")
  public static class NoticeKeywordListDTO {
    @ArraySchema(
        schema =
            @Schema(
                description = "공지사항 키워드",
                examples = {"장학", "계절학기", "졸업요건"}))
    List<String> keywords;
  }

  @Getter
  @Builder
  @Schema(title = "Notice : 비교과 공지사항 DTO")
  public static class EcNoticeDTO {
    @Schema(description = "비교과 공지사항 ID", example = "264265")
    private Long id;

    @Schema(description = "공지사항 제목", example = "자기성장 프로그램")
    private String title;

    @Schema(description = "공지사항 URL", example = "https://skuNotice.com")
    private String url;

    @Schema(description = "공지사항 사진", example = "https://skuNoticeImage.com")
    private String thumbnail;

    @Schema(description = "공지사항 발행기관", example = "대학혁신지원사업단")
    private String department;

    @Schema(description = "공지사항 날짜", example = "2025.01.08")
    private LocalDate date;
  }

  @Getter
  @Builder
  @Schema(title = "Notice : 비교과 공지사항 리스트 DTO")
  public static class EcNoticeListDTO {
    @Schema(description = "비교과 공지사항 리스트")
    List<SkuNoticeResponseDTO.EcNoticeDTO> ecNoticeList;
  }
}
