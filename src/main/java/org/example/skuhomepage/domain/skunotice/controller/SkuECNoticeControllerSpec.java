package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.exception.SkuNoticeErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "비교과 공지사항", description = "비교과 공지사항 관리 API")
@RequestMapping("/api/extra-notice/sku")
public interface SkuECNoticeControllerSpec {

  @Operation(summary = "비교과 공지사항 조회하기", description = "비교과 공지사항을 조회하는 api")
  @ApiErrorCodeExample(SkuNoticeErrorStatus.class)
  @GetMapping("/extra-curricular")
  ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(description = "페이지", example = "1")
          @RequestParam(value = "page", required = false, defaultValue = "1")
          Integer page,
      @Parameter(description = "키워드", example = "교수학습원")
          @RequestParam(value = "search_keyword", required = false, defaultValue = "all")
      ECNoticeType searchKeyword,
      @Parameter(description = "정렬 방식", example = "sort_index")
          @RequestParam(value = "sort_index", required = false, defaultValue = "view_count")
          String sortIndex,
      @Parameter(description = "정렬 순서", example = "desc")
          @RequestParam(value = "order_type", required = false, defaultValue = "desc")
          String orderType);
}
