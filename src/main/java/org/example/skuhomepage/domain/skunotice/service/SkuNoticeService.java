package org.example.skuhomepage.domain.skunotice.service;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeRequestDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkuNoticeService {

  public SkuNoticeResponseDTO.NoticeListDTO getAllSkuNotice(UserDetails userDetails) {
    return null;
  }

  public SkuNoticeResponseDTO.NoticeListDTO getSkuNoticeByKeyword(UserDetails userDetails) {
    return null;
  }

  public Boolean setSkuNoticeLike(UserDetails userDetails, Long noticeId) {
    return null;
  }

  public SkuNoticeResponseDTO.NoticeListDTO getSkuNoticeByLike(UserDetails userDetails) {
    return null;
  }

  public SkuNoticeResponseDTO.NoticeKeywordListDTO getSkuNoticeKeyword(UserDetails userDetails) {
    return null;
  }

  public Boolean postSkuNoticeKeyword(
      UserDetails userDetails, SkuNoticeRequestDTO.KeywordDTO keyword) {
    return null;
  }

  public Boolean deleteSkuNoticeKeyword(UserDetails userDetails) {
    return null;
  }

  public SkuNoticeResponseDTO.EcNoticeListDTO getSkuEcNotice(UserDetails userDetails) {
    return null;
  }
}
