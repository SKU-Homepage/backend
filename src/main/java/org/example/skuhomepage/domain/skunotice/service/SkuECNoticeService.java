package org.example.skuhomepage.domain.skunotice.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuECNoticeService {

  //  private final RestTemplate restTemplate;
  //
  //  @Value("${sku.extra-notice.url}")
  //  private String skuExtraNoticeApiUrl;
  //
  //  public SkuNoticeResponseDTO.EcNoticeListDTO getEcNoticeList(
  //      ECNoticeType searchKeyword, String sortIndex, String orderType, Long userId, Integer page)
  // {
  //
  //    return getEcNoticeListFromAPI(searchKeyword, page);
  //  }
  //
  //  @Cacheable(value = "ecNotices", key = "#searchKeyword + '-' + #page")
  //  public SkuNoticeResponseDTO.EcNoticeListDTO getEcNoticeListFromAPI(
  //      ECNoticeType searchKeyword, Integer page) {
  //    String url =
  //        UriComponentsBuilder.fromUriString(skuExtraNoticeApiUrl)
  //            .queryParam("page", page)
  //            .queryParam(
  //                "search_keyword",
  //                URLEncoder.encode(searchKeyword.getValue(), StandardCharsets.UTF_8))
  //            .queryParam("search_target", "user_name")
  //            .build(true)
  //            .toUriString();
  //
  //    log.info("[GET] 비교과 공지 API url: {}", url);
  //
  //    ResponseEntity<List<SkuNoticeApiResponseDTO.SkuNoticeApiResponse>> response =
  //        restTemplate.exchange(
  //            URI.create(url),
  //            HttpMethod.GET,
  //            new HttpEntity<>(null),
  //            new ParameterizedTypeReference<
  //                List<SkuNoticeApiResponseDTO.SkuNoticeApiResponse>>() {});
  //
  //    if (response.getStatusCode().isError()) {
  //      log.warn("구글 캘린더 API 호출 중 에러 발생: {}", response.getBody());
  //      throw new GeneralException(SkuEcNoticeErrorStatus.SKU_NOTICE_API_CALL_FAILURE);
  //    }
  //
  //    return
  // SkuNoticeApiResponseDTO.builder().responseList(response.getBody()).build().toDTOList();
  //  }
}
