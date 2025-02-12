package org.example.skuhomepage.domain.skunotice.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeRequestDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.service.SkuNoticeService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SkuNoticeController implements SkuNoticeControllerSpec {

  private final SkuNoticeService skuNoticeService;

  @Override
  public ApiResponse<SkuNoticeResponseDTO.NoticeListDTO> getSkuNotice(
      UserDetails userDetails, String keyword) {

    SkuNoticeResponseDTO.NoticeListDTO response =
        keyword == null || keyword.isEmpty() || keyword.isBlank()
            ? skuNoticeService.getAllSkuNotice(userDetails)
            : skuNoticeService.getSkuNoticeByKeyword(userDetails);
    return ApiResponse.onSuccess(
        keyword == null || keyword.isEmpty() || keyword.isBlank()
            ? SkuNoticeResponseDTO.NoticeListDTO.builder()
                .timestamp(LocalDateTime.now())
                .noticeList(
                    List.of(
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265308L)
                            .title(" 2025학년도 1학기 전과, 재입학 발표안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265308")
                            .department("교무처")
                            .date(java.time.LocalDate.of(2025, 2, 12))
                            .like(true)
                            .build(),
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265296L)
                            .title("25-1차 군 가산복무 지원금 지급대상자(부사관) 모집계획 안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265296")
                            .department("학생처")
                            .date(java.time.LocalDate.of(2025, 2, 11))
                            .like(false)
                            .build(),
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265294L)
                            .title("2025년도 (재)강서구장학회 지역사회기여 장학생 선발 안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265294")
                            .department("학생처")
                            .date(java.time.LocalDate.of(2025, 2, 11))
                            .like(true)
                            .build()))
                .build()
            : skuNoticeService.getSkuNoticeByKeyword(userDetails));
  }

  @Override
  public ApiResponse<Boolean> setSkuNoticeLike(UserDetails userDetails, Long noticeId) {

    Boolean response = skuNoticeService.setSkuNoticeLike(userDetails, noticeId);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.NoticeListDTO> getSkuNoticeByLike(
      UserDetails userDetails, String order) {

    SkuNoticeResponseDTO.NoticeListDTO response = skuNoticeService.getSkuNoticeByLike(userDetails);
    return ApiResponse.onSuccess(
        SkuNoticeResponseDTO.NoticeListDTO.builder()
            .timestamp(null)
            .noticeList(
                order == null || !order.equals("asc")
                    ? List.of(
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265308L)
                            .title(" 2025학년도 1학기 전과, 재입학 발표안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265308")
                            .department("교무처")
                            .date(java.time.LocalDate.of(2025, 2, 12))
                            .like(true)
                            .build(),
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265294L)
                            .title("2025년도 (재)강서구장학회 지역사회기여 장학생 선발 안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265294")
                            .department("학생처")
                            .date(java.time.LocalDate.of(2025, 2, 11))
                            .like(true)
                            .build())
                    : List.of(
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265294L)
                            .title("2025년도 (재)강서구장학회 지역사회기여 장학생 선발 안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265294")
                            .department("학생처")
                            .date(java.time.LocalDate.of(2025, 2, 11))
                            .like(true)
                            .build(),
                        SkuNoticeResponseDTO.NoticeDTO.builder()
                            .id(265308L)
                            .title(" 2025학년도 1학기 전과, 재입학 발표안내")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&document_srl=265308")
                            .department("교무처")
                            .date(java.time.LocalDate.of(2025, 2, 12))
                            .like(true)
                            .build()))
            .build());
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.NoticeKeywordListDTO> getSkuNoticeKeyword(
      UserDetails userDetails) {

    SkuNoticeResponseDTO.NoticeKeywordListDTO response =
        skuNoticeService.getSkuNoticeKeyword(userDetails);
    return ApiResponse.onSuccess(
        SkuNoticeResponseDTO.NoticeKeywordListDTO.builder()
            .keywords(List.of("장학", "계절학기", "졸업요건"))
            .build());
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.NoticeKeywordListDTO> postSkuNoticeKeyword(
      UserDetails userDetails, SkuNoticeRequestDTO.KeywordDTO keyword) {

    Boolean response = skuNoticeService.postSkuNoticeKeyword(userDetails, keyword);
    return ApiResponse.onSuccess(
        SkuNoticeResponseDTO.NoticeKeywordListDTO.builder()
            .keywords(
                List.of("장학", "계절학기", "졸업요건").contains(keyword.keyword())
                    ? List.of("장학", "계절학기", "졸업요건")
                    : List.of("장학", "계절학기", "졸업요건", keyword.keyword()))
            .build());
  }

  @Override
  public ApiResponse<Void> deleteSkuNoticeKeyword(UserDetails userDetails) {

    Boolean response = skuNoticeService.deleteSkuNoticeKeyword(userDetails);
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(
      UserDetails userDetails, String order) {

    SkuNoticeResponseDTO.EcNoticeListDTO response = skuNoticeService.getSkuEcNotice(userDetails);
    return ApiResponse.onSuccess(
        SkuNoticeResponseDTO.EcNoticeListDTO.builder()
            .ecNoticeList(
                order == null || !order.equals("asc")
                    ? List.of(
                        SkuNoticeResponseDTO.EcNoticeDTO.builder()
                            .id(264483L)
                            .title("[대플]2025학년도 진로 ⬝ 취업 인식 및 수요조사 이벤트")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&page=2&document_srl=264483")
                            .thumbnail(
                                "https://www.skuniv.ac.kr/files/attach/images/78/483/264/544708c83dfa0a60691028d28911af79.jpg")
                            .department("진로취업지원센터")
                            .date(java.time.LocalDate.of(2025, 1, 22))
                            .build(),
                        SkuNoticeResponseDTO.EcNoticeDTO.builder()
                            .id(264265L)
                            .title("2025년 상반기 ICT 학점연계 프로젝트 인턴십 참여학생 모집 안내 file")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&page=2&document_srl=264265")
                            .thumbnail(
                                "https://www.skuniv.ac.kr/files/attach/images/78/265/264/e0da85ff9ca27e3c7a182966786219bd.jpg")
                            .department("현장실습지원센터")
                            .date(java.time.LocalDate.of(2025, 1, 20))
                            .build())
                    : List.of(
                        SkuNoticeResponseDTO.EcNoticeDTO.builder()
                            .id(264265L)
                            .title("2025년 상반기 ICT 학점연계 프로젝트 인턴십 참여학생 모집 안내 file")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&page=2&document_srl=264265")
                            .thumbnail(
                                "https://www.skuniv.ac.kr/files/attach/images/78/265/264/e0da85ff9ca27e3c7a182966786219bd.jpg")
                            .department("현장실습지원센터")
                            .date(java.time.LocalDate.of(2025, 1, 20))
                            .build(),
                        SkuNoticeResponseDTO.EcNoticeDTO.builder()
                            .id(264483L)
                            .title("[대플]2025학년도 진로 ⬝ 취업 인식 및 수요조사 이벤트")
                            .url(
                                "https://www.skuniv.ac.kr/index.php?mid=notice&page=2&document_srl=264483")
                            .thumbnail(
                                "https://www.skuniv.ac.kr/files/attach/images/78/483/264/544708c83dfa0a60691028d28911af79.jpg")
                            .department("진로취업지원센터")
                            .date(java.time.LocalDate.of(2025, 1, 22))
                            .build()))
            .build());
  }
}
