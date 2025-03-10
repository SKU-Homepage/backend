package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.enums.SortIndex;
import org.example.skuhomepage.domain.skunotice.exception.SkuNoticeErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "비교과 공지사항", description = "비교과 공지사항 관리 API")
@RequestMapping("/api/ec-notices/sku")
public interface SkuECNoticeControllerSpec {

  @Operation(summary = "비교과 공지사항 조회하기", description = "비교과 공지사항을 조회하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping
  ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          CustomUserDetails userDetails,
      @Parameter(description = "페이지", example = "1")
          @RequestParam(value = "page", defaultValue = "1")
          int page,
      @Parameter(description = "키워드", example = "제목 검색")
          @RequestParam(value = "search_keyword", required = false, defaultValue = "all")
          String searchKeyword,
      @Parameter(description = "작성자", example = "ALL")
          @RequestParam(value = "author", required = false, defaultValue = "ALL")
          ECNoticeType author,
      @Parameter(description = "정렬 방식", example = "sort_index")
          @RequestParam(value = "sort_index", required = false, defaultValue = "DATE")
          SortIndex sortIndex);

  @Operation(summary = "공지사항 조회수 증가", description = "공지사항 조회수를 증가하는 api")
  @PatchMapping("/{ecNoticeId}/view-count")
  ApiResponse<Void> increaseViewCount(
      @Parameter(description = "공지사항 ID", example = "1", required = true)
          @RequestParam(value = "ecNoticeId")
          Long ecNoticeId);

  //  @Operation(summary = "공지사항 전체 저장", description = "공지사항을 전체 저장하는 api")
  //  @PostMapping()
  //  ApiResponse<Void> saveSkuEcNotice(
  //      @Parameter(description = "페이지", example = "1")
  //          @RequestParam(value = "page", required = false, defaultValue = "1")
  //          int page);
}
