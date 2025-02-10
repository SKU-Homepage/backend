package org.example.skuhomepage.domain.skunotice.service;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeRequestDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface SkuNoticeService {

  SkuNoticeResponseDTO.NoticeListDTO getAllSkuNotice(UserDetails userDetails);

  SkuNoticeResponseDTO.NoticeListDTO getSkuNoticeByKeyword(UserDetails userDetails);

  Boolean setSkuNoticeLike(UserDetails userDetails, Long noticeId);

  SkuNoticeResponseDTO.NoticeListDTO getSkuNoticeByLike(UserDetails userDetails);

  SkuNoticeResponseDTO.NoticeKeywordListDTO getSkuNoticeKeyword(UserDetails userDetails);

  Boolean postSkuNoticeKeyword(UserDetails userDetails, SkuNoticeRequestDTO.KeywordDTO keyword);

  Boolean deleteSkuNoticeKeyword(UserDetails userDetails);

  SkuNoticeResponseDTO.EcNoticeListDTO getSkuEcNotice(UserDetails userDetails);
}
