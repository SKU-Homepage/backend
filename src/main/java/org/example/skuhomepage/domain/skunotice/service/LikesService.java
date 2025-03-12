package org.example.skuhomepage.domain.skunotice.service;

import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.domain.skunotice.entity.Likes;
import org.example.skuhomepage.domain.skunotice.repository.LikesRepository;
import org.example.skuhomepage.domain.skunotice.repository.SkuNoticeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

  private final LikesRepository likesRepository;
  private final UserRepository userRepository;
  private final SkuNoticeRepository skuNoticeRepository;

  public void toggleLikes(Long userId, Long noticeId) {
    likesRepository
        .findByUserIdAndSkuNoticeId(userId, noticeId)
        .ifPresentOrElse(
            likesRepository::delete,
            () ->
                likesRepository.save(
                    new Likes(
                        userRepository.findById(userId).orElseThrow(null),
                        skuNoticeRepository.findById(noticeId).orElseThrow(null))));
  }

  public void getNoticeByLikesAndUser(Long userId) {
    likesRepository.findNoticeLikesByUser(userId, Pageable.unpaged());
  }
}
