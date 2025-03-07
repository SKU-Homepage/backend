package org.example.skuhomepage.domain.skunotice.controller;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.service.SkuECNoticeService;
import org.example.skuhomepage.domain.skunotice.service.SkuNoticeScrapService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SkuECNoticeController implements SkuECNoticeControllerSpec {

  private final SkuECNoticeService skuECNoticeService;
  private final SkuNoticeScrapService skuNoticeScrapService;

  @Override
  public ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(
      UserDetails userDetails,
      Integer page,
      ECNoticeType searchKeyword,
      String sortIndex,
      String orderType) {

    //    return ApiResponse.onSuccess(
    //        skuECNoticeService.getEcNoticeList(searchKeyword, sortIndex, orderType, 1L, page));
    return ApiResponse.onSuccess(null);
  }

  @Override
  public ApiResponse<Void> saveSkuEcNotice() {

    skuNoticeScrapService.saveAll(1, 427);

    return ApiResponse.onSuccess(null);
  }
}
