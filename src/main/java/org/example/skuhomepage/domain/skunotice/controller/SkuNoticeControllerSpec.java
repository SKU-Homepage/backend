package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.exception.SkuNoticeErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공지사항", description = "공지사항 관리 API")
@RequestMapping("/api/notices/sku")
public interface SkuNoticeControllerSpec {

  @Operation(summary = "공지사항 조회하기", description = "공지사항 전체 조회, 공지사항 검색을 위한 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping
  ApiResponse<SkuNoticeResponseDTO.SkuNoticeListDTO> getSkuNotice(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          CustomUserDetails userDetails,
      @Parameter(description = "키워드") @RequestParam(value = "search_keyword", required = false)
          String searchKeyword,
      @Parameter(description = "페이지", example = "0")
          @RequestParam(value = "page", defaultValue = "0")
          int page);

  @Operation(summary = "공지사항 찜 등록하기", description = "공지사항 찜을 설정하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @PostMapping("{noticeId}/likes")
  ApiResponse<Boolean> setSkuNoticeLike(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          CustomUserDetails userDetails,
      @Parameter(description = "찜 누를 공지사항 Id", example = "267424", required = true) @PathVariable
          Long noticeId);

  @Operation(summary = "공지사항 찜목록 조회하기", description = "공지사항 찜목록을 조회하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping("/likes")
  ApiResponse<SkuNoticeResponseDTO.SkuNoticeListDTO> getSkuNoticeByLike(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          CustomUserDetails userDetails,
      @Parameter(description = "페이지", example = "0")
          @RequestParam(value = "page", defaultValue = "0")
          int page);
}
