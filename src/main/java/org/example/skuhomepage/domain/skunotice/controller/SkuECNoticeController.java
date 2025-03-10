package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.enums.SortIndex;
import org.example.skuhomepage.domain.skunotice.service.SkuECNoticeService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.security.CustomUserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SkuECNoticeController implements SkuECNoticeControllerSpec {

  private final SkuECNoticeService skuECNoticeService;

  @Override
  public ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(
      CustomUserDetails userDetails,
      int page,
      String searchKeyword,
      ECNoticeType author,
      SortIndex sortIndex) {

    return ApiResponse.onSuccess(
        skuECNoticeService.getEcNoticeList(
            searchKeyword, author, sortIndex, userDetails.getUserId(), page));
  }

  @Override
  public ApiResponse<Void> increaseViewCount(Long ecNoticeId) {

    skuECNoticeService.increaseViewCount(ecNoticeId);

    return ApiResponse.onSuccess(null);
  }

  //  @Override
  //  public ApiResponse<Void> saveSkuEcNotice(int page) {
  //
  //    skuNoticeScrapService.saveAll(page, 427);
  //
  //    return ApiResponse.onSuccess(null);
  //  }
}
