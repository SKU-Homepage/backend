package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.service.SkuNoticeService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SkuNoticeController implements SkuNoticeControllerSpec {

  private final SkuNoticeService skuNoticeService;

  @Override
  public ApiResponse<SkuNoticeResponseDTO.SkuNoticeListDTO> getSkuNotice(
      CustomUserDetails userDetails, String keyword, int page) {

    SkuNoticeResponseDTO.SkuNoticeListDTO response =
        keyword == null || keyword.isEmpty() || keyword.isBlank()
            ? skuNoticeService.getAllSkuNotice(userDetails.getUserId(), page)
            : skuNoticeService.getSkuNoticeByKeyword(userDetails.getUserId(), keyword, page);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<Boolean> setSkuNoticeLike(CustomUserDetails userDetails, Long noticeId) {

    Boolean response = skuNoticeService.setSkuNoticeLike(userDetails.getUserId(), noticeId);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.SkuNoticeListDTO> getSkuNoticeByLike(
      CustomUserDetails userDetails, int page) {

    SkuNoticeResponseDTO.SkuNoticeListDTO response =
        skuNoticeService.getSkuNoticeByLike(userDetails.getUserId(), page);
    return ApiResponse.onSuccess(response);
  }
}
