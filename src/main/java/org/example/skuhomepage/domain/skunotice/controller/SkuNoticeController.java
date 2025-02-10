package org.example.skuhomepage.domain.skunotice.controller;

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
        keyword.isEmpty()
            ? skuNoticeService.getAllSkuNotice(userDetails)
            : skuNoticeService.getSkuNoticeByKeyword(userDetails);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<Boolean> setSkuNoticeLike(UserDetails userDetails, Long noticeId) {

    Boolean response = skuNoticeService.setSkuNoticeLike(userDetails, noticeId);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.NoticeListDTO> getSkuNoticeByLike(
      UserDetails userDetails) {

    SkuNoticeResponseDTO.NoticeListDTO response = skuNoticeService.getSkuNoticeByLike(userDetails);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.NoticeKeywordListDTO> getSkuNoticeKeyword(
      UserDetails userDetails) {

    SkuNoticeResponseDTO.NoticeKeywordListDTO response =
        skuNoticeService.getSkuNoticeKeyword(userDetails);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<Boolean> postSkuNoticeKeyword(
      UserDetails userDetails, SkuNoticeRequestDTO.KeywordDTO keyword) {

    Boolean response = skuNoticeService.postSkuNoticeKeyword(userDetails, keyword);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<Boolean> deleteSkuNoticeKeyword(UserDetails userDetails) {

    Boolean response = skuNoticeService.deleteSkuNoticeKeyword(userDetails);
    return ApiResponse.onSuccess(response);
  }

  @Override
  public ApiResponse<SkuNoticeResponseDTO.EcNoticeListDTO> getSkuEcNotice(UserDetails userDetails) {

    SkuNoticeResponseDTO.EcNoticeListDTO response = skuNoticeService.getSkuEcNotice(userDetails);
    return ApiResponse.onSuccess(response);
  }
}
