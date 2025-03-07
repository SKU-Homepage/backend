package org.example.skuhomepage.domain.skunotice.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.transaction.Transactional;

import org.example.skuhomepage.domain.skunotice.SkuNoticeRepository;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeApiResponseDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeApiResponseDTO.SkuNoticeApiResponse;
import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.exception.SkuEcNoticeErrorStatus;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuNoticeScrapService {

  private final RestTemplate restTemplate;
  private final SkuNoticeRepository skuNoticeRepository;

  @Value("${sku.notice.url}")
  private String skuNoticeApiUrl;

  public SkuNoticeApiResponseDTO scrap(int page, ECNoticeType searchKeyword) {
    String url =
        UriComponentsBuilder.fromUriString(skuNoticeApiUrl)
            .queryParam("page", page)
            .queryParam(
                "search_keyword",
                URLEncoder.encode(
                    searchKeyword == null ? "" : searchKeyword.getValue(), StandardCharsets.UTF_8))
            .queryParam("search_target", "user_name")
            .build(true)
            .toUriString();

    log.info("[GET] 공지 API url: {}", url);

    ResponseEntity<List<SkuNoticeApiResponse>> response =
        restTemplate.exchange(
            URI.create(url),
            HttpMethod.GET,
            new HttpEntity<>(null),
            new ParameterizedTypeReference<List<SkuNoticeApiResponse>>() {});

    if (response.getStatusCode().isError()) {
      log.warn("서경대 공지 스크랩 API 호출 중 에러 발생: {}", response.getBody());
      throw new GeneralException(SkuEcNoticeErrorStatus.SKU_NOTICE_API_CALL_FAILURE);
    }

    return SkuNoticeApiResponseDTO.builder().responseList(response.getBody()).build();
  }

  public void save(SkuNoticeApiResponseDTO listDTO) {

    AtomicInteger existsCount = new AtomicInteger();

    List<SkuNotice> skuNoticeList = new ArrayList<>();
    listDTO
        .getResponseList()
        .forEach(
            dto -> {
              skuNoticeList.add(dto.toEntity());
            });
    for (SkuNotice skuNotice : skuNoticeList) {
      skuNoticeRepository
          .findById(skuNotice.getId())
          .ifPresentOrElse(
              notice -> {
                skuNoticeRepository.save(
                    SkuNotice.builder()
                        .id(skuNotice.getId())
                        .title(skuNotice.getTitle())
                        .date(skuNotice.getDate())
                        .category(skuNotice.getCategory())
                        .author(skuNotice.getAuthor())
                        .view_count(notice.getView_count())
                        .image(skuNotice.getImage())
                        .url(skuNotice.getUrl())
                        .build());
                existsCount.getAndIncrement();
              },
              () -> skuNoticeRepository.save(skuNotice));
    }
    log.info("새로 추가: {}, 존재: {}", skuNoticeList.size() - existsCount.get(), existsCount.get());
  }

  @Transactional
  public void saveAll(int startPage, int endPage) {

    for (int i = startPage; i <= endPage; i++) {
      save(scrap(i, null));
      log.info("{} 페이지 저장.", i);
    }
  }
}
