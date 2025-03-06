package org.example.skuhomepage.domain.skunotice.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.example.skuhomepage.domain.skunotice.dto.SkuECNoticeApiResponseDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.exception.SkuEcNoticeErrorStatus;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
public class SkuECNoticeService {

  private final RestTemplate restTemplate;

  @Value("${sku.extra-notice.url}")
  private String skuExtraNoticeApiUrl;

  public SkuNoticeResponseDTO.EcNoticeListDTO getEcNoticeList(
      ECNoticeType searchKeyword,
      String sortIndex,
      String orderType,
      Long userId,
      Integer page) {

    return getEcNoticeListFromAPI(searchKeyword, page);
  }

  @Cacheable(value = "ecNotices", key = "#searchKeyword + '-' + #page")
  public SkuNoticeResponseDTO.EcNoticeListDTO getEcNoticeListFromAPI(
          ECNoticeType searchKeyword, Integer page) {
    String url =
        UriComponentsBuilder.fromUriString(skuExtraNoticeApiUrl)
            .queryParam("page", page)
            .queryParam(
                "search_keyword",
                URLEncoder.encode(searchKeyword.getValue(), StandardCharsets.UTF_8))
            .queryParam("search_target", "user_name")
            .build(true)
            .toUriString();

    log.info("[GET] 비교과 공지 API url: {}", url);

    ResponseEntity<List<SkuECNoticeApiResponseDTO.SkuExtraNoticeApiResponse>> response =
        restTemplate.exchange(
            URI.create(url),
            HttpMethod.GET,
            new HttpEntity<>(null),
            new ParameterizedTypeReference<
                List<SkuECNoticeApiResponseDTO.SkuExtraNoticeApiResponse>>() {});

    if (response.getStatusCode().isError()) {
      log.warn("구글 캘린더 API 호출 중 에러 발생: {}", response.getBody());
      throw new GeneralException(SkuEcNoticeErrorStatus.EC_NOTICE_API_CALL_FAILURE);
    }

    return SkuECNoticeApiResponseDTO.builder()
        .responseList(response.getBody())
        .build()
        .toEcNoticeDTOList();
  }
}
