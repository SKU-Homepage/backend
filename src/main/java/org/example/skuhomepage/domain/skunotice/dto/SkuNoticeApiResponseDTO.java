package org.example.skuhomepage.domain.skunotice.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkuNoticeApiResponseDTO {

  List<SkuNoticeApiResponse> responseList;

  @Getter
  @Builder
  public static class SkuNoticeApiResponse {
    private String id;
    private String category;
    private String date;
    private String title;
    private String url;
    private String author;

    @JsonProperty(value = "view_count")
    private String viewCount;

    private String image;

    public SkuNotice toEntity() {
      return SkuNotice.builder()
          .id(Long.valueOf(this.id))
          .category(this.category)
          .date(LocalDateTime.parse(this.date))
          .title(this.title)
          .url(this.url)
          .author(this.author)
          .view_count(0)
          .build();
    }
  }
}
