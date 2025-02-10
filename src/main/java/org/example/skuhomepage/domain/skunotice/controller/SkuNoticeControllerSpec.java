package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeRequestDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO.NoticeListDTO;
import org.example.skuhomepage.domain.skunotice.exception.SkuNoticeErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "notice-controller", description = "공지사항 관리 API")
@RequestMapping("/api/notices/sku")
public interface SkuNoticeControllerSpec {

  @Operation(summary = "공지사항 조회하기", description = "공지사항 전체 조회, 키워드로 공지사항 조회를 위한 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping
  ApiResponse<NoticeListDTO> getSkuNotice(
      @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(description = "키워드", example = "장학") @RequestParam String keyword);

  @Operation(summary = "공지사항 찜 등록하기", description = "공지사항 찜을 설정하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @PostMapping("{noticeId}/likes")
  ApiResponse<Boolean> setSkuNoticeLike(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(description = "찜 누를 공지사항 Id", example = "1", required = true) @PathVariable
          Long noticeId);

  @Operation(summary = "공지사항 찜목록 조회하기", description = "공지사항 찜목록을 조회하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping("/likes")
  ApiResponse<SkuNoticeResponseDTO.NoticeListDTO> getSkuNoticeByLike(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @Operation(summary = "키워드 조회하기", description = "공지사항 키워드를 모두 조회하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping("/keywords")
  ApiResponse<SkuNoticeResponseDTO.NoticeKeywordListDTO> getSkuNoticeKeyword(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @Operation(summary = "공지사항 키워드 등록하기", description = "공지사항 키워드를 등록하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @PostMapping("/keywords")
  ApiResponse<Boolean> postSkuNoticeKeyword(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(description = "공지사항 키워드", example = "장학", required = true)
          SkuNoticeRequestDTO.KeywordDTO keyword);

  @Operation(summary = "공지사항 키워드 삭제하기", description = "공지사항 키워드를 삭제하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @DeleteMapping("/keywords")
  ApiResponse<Boolean> deleteSkuNoticeKeyword(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @Operation(summary = "비교과 공지사항 조회하기", description = "비교과 공지사항을 조회하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping("/extra-curricular")
  ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);
}
