package org.example.skuhomepage.domain.skunotice.repository;

import java.util.List;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.skunotice.entity.Likes;
import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

  @Query(
      "SELECT l.skuNotice FROM Likes l WHERE l.user = :user "
          + "AND l.skuNotice.author = '교수학습원' AND l.skuNotice.author = '진로취업지원센터' "
          + "AND l.skuNotice.author = '대학혁신지원사업단'")
  List<SkuNotice> findECNoticeLikesByUser(User user);

  boolean existsByUserIdAndSkuNotice(Long userId, SkuNotice skuNotice);
}
