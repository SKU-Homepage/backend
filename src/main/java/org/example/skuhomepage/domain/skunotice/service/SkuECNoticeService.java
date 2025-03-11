package org.example.skuhomepage.domain.skunotice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO;
import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.enums.SortIndex;
import org.example.skuhomepage.domain.skunotice.repository.LikesRepository;
import org.example.skuhomepage.domain.skunotice.repository.SkuNoticeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuECNoticeService {

  private final SkuNoticeRepository skuNoticeRepository;
  private final LikesRepository likesRepository;

  public SkuNoticeResponseDTO.EcNoticeListDTO getEcNoticeList(
      String searchKeyword, ECNoticeType ecNoticeType, SortIndex sortIndex, Long userId, int page) {

    Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("date")));

    Pageable pageableByViewCount =
        PageRequest.of(page, 10, Sort.by(Sort.Order.desc("view_count"), Sort.Order.desc("date")));

    Pageable pageableByLikeCount =
        PageRequest.of(page, 10, Sort.by(Sort.Order.desc("like_count"), Sort.Order.desc("date")));

    List<String> authors =
        List.of(
            ECNoticeType.GYOSU_HAKSEUB.getValue(),
            ECNoticeType.JINLO_CHWIEOB.getValue(),
            ECNoticeType.DAEHAK_HYEOKSIN.getValue());

    if (ecNoticeType != ECNoticeType.ALL) {
      authors = List.of(ecNoticeType.getValue());
    }

    List<SkuNoticeResponseDTO.EcNoticeDTO> noticeList =
        switch (sortIndex) {
          case VIEW_COUNT -> skuNoticeRepository
              .findAllECNoticesByTitleOrderByViewCount(searchKeyword, authors, pageableByViewCount)
              .stream()
              .map(notice -> mapToDTO(notice, userId))
              .collect(Collectors.toList());

          case LIKE_COUNT -> skuNoticeRepository
              .findAllECNoticesByTitleOrderByLikeCount(searchKeyword, authors, pageableByLikeCount)
              .stream()
              .map(notice -> mapToDTO(notice, userId))
              .collect(Collectors.toList());

          default -> skuNoticeRepository
              .findAllECNoticesByTitleOrderByDate(searchKeyword, authors, pageable)
              .stream()
              .map(notice -> mapToDTO(notice, userId))
              .collect(Collectors.toList());
        };

    return SkuNoticeResponseDTO.EcNoticeListDTO.builder().ecNoticeList(noticeList).build();
  }

  private SkuNoticeResponseDTO.EcNoticeDTO mapToDTO(SkuNotice notice, Long userId) {
    boolean isLiked = likesRepository.existsByUserIdAndSkuNotice(userId, notice);
    return SkuNoticeResponseDTO.EcNoticeDTO.from(notice, isLiked);
  }

  public void increaseViewCount(Long ecNoticeId) {
    SkuNotice notice = skuNoticeRepository.findById(ecNoticeId).orElseThrow(null);
    // 예외처리 추가 예쩡

    notice.setViewCount(notice.getViewCount() + 1);
    skuNoticeRepository.save(notice);
  }
}
