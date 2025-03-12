package org.example.skuhomepage.domain.skunotice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO.SkuNoticeDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO.SkuNoticeListDTO;
import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.example.skuhomepage.domain.skunotice.exception.SkuNoticeErrorStatus;
import org.example.skuhomepage.domain.skunotice.repository.LikesRepository;
import org.example.skuhomepage.domain.skunotice.repository.SkuNoticeRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkuNoticeService {

  private final UserRepository userRepository;
  private final SkuNoticeRepository skuNoticeRepository;
  private final LikesRepository likesRepository;
  private final LikesService likesService;

  public SkuNoticeListDTO getAllSkuNotice(Long userId, int page) {

    Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("date")));

    Page<SkuNotice> noticePage = skuNoticeRepository.findAll(pageable);

    List<SkuNoticeResponseDTO.SkuNoticeDTO> noticeList =
        noticePage.getContent().stream()
            .map(
                skuNotice ->
                    SkuNoticeDTO.from(
                        skuNotice, likesRepository.existsByUserIdAndSkuNotice(userId, skuNotice)))
            .collect(Collectors.toList());

    return SkuNoticeResponseDTO.SkuNoticeListDTO.builder().skuNoticeList(noticeList).build();
  }

  public SkuNoticeListDTO getSkuNoticeByKeyword(Long userId, String keyword, int page) {

    Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("date")));

    Page<SkuNotice> noticePage = skuNoticeRepository.findByKeyword(keyword, pageable);

    List<SkuNoticeResponseDTO.SkuNoticeDTO> noticeList =
        noticePage.getContent().stream()
            .map(
                skuNotice ->
                    SkuNoticeDTO.from(
                        skuNotice, likesRepository.existsByUserIdAndSkuNotice(userId, skuNotice)))
            .collect(Collectors.toList());

    return SkuNoticeResponseDTO.SkuNoticeListDTO.builder().skuNoticeList(noticeList).build();
  }

  public Boolean setSkuNoticeLike(Long userId, Long noticeId) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    SkuNotice skuNotice =
        skuNoticeRepository
            .findById(noticeId)
            .orElseThrow(() -> new GeneralException(SkuNoticeErrorStatus.NOTICE_NOT_FOUND));

    boolean isLiked = likesRepository.existsByUserIdAndSkuNotice(userId, skuNotice);

    likesService.toggleLikes(userId, noticeId);

    return !isLiked;
  }

  public SkuNoticeListDTO getSkuNoticeByLike(Long userId, int page) {

    Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("l.skuNotice.date")));

    Page<SkuNotice> noticePage = likesRepository.findNoticeLikesByUser(userId, pageable);

    List<SkuNoticeResponseDTO.SkuNoticeDTO> noticeList =
        noticePage.getContent().stream()
            .map(
                skuNotice ->
                    SkuNoticeDTO.from(
                        skuNotice, likesRepository.existsByUserIdAndSkuNotice(userId, skuNotice)))
            .collect(Collectors.toList());

    return SkuNoticeResponseDTO.SkuNoticeListDTO.builder().skuNoticeList(noticeList).build();
  }
}
