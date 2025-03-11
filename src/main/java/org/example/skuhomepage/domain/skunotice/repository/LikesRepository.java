package org.example.skuhomepage.domain.skunotice.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.skunotice.entity.Likes;
import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

  @Query(
      "SELECT l.skuNotice FROM Likes l WHERE l.user.Id = :userId "
          + "AND l.skuNotice.author = '교수학습원' AND l.skuNotice.author = '진로취업지원센터' "
          + "AND l.skuNotice.author = '대학혁신지원사업단'")
  List<SkuNotice> findECNoticeLikesByUser(Long userId);

  @Query(
      "SELECT l.skuNotice FROM Likes l WHERE l.user.Id = :userId "
          + "AND l.skuNotice.author != '교수학습원' AND l.skuNotice.author != '진로취업지원센터' "
          + "AND l.skuNotice.author != '대학혁신지원사업단'")
  List<SkuNotice> findNoticeLikesByUser(Long userId);

  boolean existsByUserIdAndSkuNotice(Long userId, SkuNotice skuNotice);

  Optional<Likes> findByUserIdAndSkuNoticeId(Long userId, Long noticeId);
}
