package org.example.skuhomepage.domain.skunotice.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkuECNoticeApiResponseDTO {

  List<SkuExtraNoticeApiResponse> responseList;

  @Getter
  @Builder
  public static class SkuExtraNoticeApiResponse {
    private String id;
    private String category;
    private String date;
    private String title;
    private String url;
    private String author;

    @JsonProperty(value = "view_count")
    private String viewCount;

    private String image;
  }

  public SkuNoticeResponseDTO.EcNoticeListDTO toEcNoticeDTOList() {
    List<SkuNoticeResponseDTO.EcNoticeDTO> dtoList = new ArrayList<>();
    this.responseList.forEach(
        res ->
            dtoList.add(
                SkuNoticeResponseDTO.EcNoticeDTO.builder()
                    .id(Long.valueOf(res.id))
                    .title(res.title)
                    .url(res.url)
                    .thumbnail(res.image)
                    .department(res.author)
                    .date(LocalDateTime.parse(res.date, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build()));

    return SkuNoticeResponseDTO.EcNoticeListDTO.builder().ecNoticeList(dtoList).build();
  }
}
