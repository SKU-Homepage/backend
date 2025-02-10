package org.example.skuhomepage.domain.skunotice.service;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeRequestDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkuNoticeServiceImpl implements SkuNoticeService {

  @Override
  public SkuNoticeResponseDTO.NoticeListDTO getAllSkuNotice(UserDetails userDetails) {
    return null;
  }

  @Override
  public SkuNoticeResponseDTO.NoticeListDTO getSkuNoticeByKeyword(UserDetails userDetails) {
    return null;
  }

  @Override
  public Boolean setSkuNoticeLike(UserDetails userDetails, Long noticeId) {
    return null;
  }

  @Override
  public SkuNoticeResponseDTO.NoticeListDTO getSkuNoticeByLike(UserDetails userDetails) {
    return null;
  }

  @Override
  public SkuNoticeResponseDTO.NoticeKeywordListDTO getSkuNoticeKeyword(UserDetails userDetails) {
    return null;
  }

  @Override
  public Boolean postSkuNoticeKeyword(
      UserDetails userDetails, SkuNoticeRequestDTO.KeywordDTO keyword) {
    return null;
  }

  @Override
  public Boolean deleteSkuNoticeKeyword(UserDetails userDetails) {
    return null;
  }

  @Override
  public SkuNoticeResponseDTO.EcNoticeListDTO getSkuEcNotice(UserDetails userDetails) {
    return null;
  }
}
