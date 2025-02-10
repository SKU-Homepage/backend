package org.example.skuhomepage.domain.skunotice.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class SkuNoticeResponseDTO {

  @Getter
  @Schema(title = "Notice : 공지사항 DTO")
  public static class NoticeDTO {
    @Schema(description = "공지사항 제목", example = "2024 콘텐츠 인사이트:상상은 현실이 된다")
    private String title;

    @Schema(description = "공지사항 URL", example = "https://skuNotice.com")
    private String url;

    @Schema(description = "공지사항 좋아요", example = "true")
    private boolean like;
  }

  @Getter
  @Schema(title = "Notice : 공지사항 리스트 DTO")
  public static class NoticeListDTO {
    @Schema(description = "전체 공지사항 리스트")
    List<SkuNoticeResponseDTO.NoticeDTO> noticeList;
  }

  @Getter
  @Schema(title = "Notice : 공지사항 키워드 리스트 DTO")
  public static class NoticeKeywordListDTO {
    @Schema(description = "공지사항 키워드 리스트")
    List<String> keywords;
  }

  @Getter
  @Schema(title = "Notice : 비교과 공지사항 DTO")
  public static class EcNoticeDTO {
    @Schema(description = "공지사항 제목", example = "자기성장 프로그램")
    private String title;

    @Schema(description = "공지사항 URL", example = "https://skuNotice.com")
    private String url;

    @Schema(description = "공지사항 발행기관", example = "교수학습원")
    private String department;

    @Schema(description = "공지사항 날짜", example = "2025.01.08 ~ 2025.01.22")
    private String date;
  }

  @Getter
  @Schema(title = "Notice : 비교과 공지사항 리스트 DTO")
  public static class EcNoticeListDTO {
    @Schema(description = "비교과 공지사항 리스트")
    List<SkuNoticeResponseDTO.EcNoticeDTO> EcNoticeList;
  }
}
