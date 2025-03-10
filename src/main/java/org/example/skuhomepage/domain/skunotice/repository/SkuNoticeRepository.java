package org.example.skuhomepage.domain.skunotice.repository;

import java.util.List;

import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkuNoticeRepository extends JpaRepository<SkuNotice, Long> {

  // 날짜 기준 정렬
  @Query(
      value =
          "SELECT * FROM sku_notice s WHERE s.author IN (:authors) "
              + "AND (:title IS NULL OR :title = '' OR s.title LIKE CONCAT('%', :title, '%')) "
              + "ORDER BY s.date DESC",
      nativeQuery = true)
  List<SkuNotice> findAllECNoticesByTitleOrderByDate(
      @Param("title") String title, @Param("authors") List<String> authors, Pageable pageable);

  // 조회수 기준 정렬
  @Query(
      value =
          "SELECT * FROM SkuNotice s WHERE s.author IN (:authors) "
              + "AND (:title IS NULL OR :title = '' OR s.title LIKE CONCAT('%', :title, '%')) "
              + "ORDER BY s.date DESC, s.viewCount DESC",
      nativeQuery = true)
  List<SkuNotice> findAllECNoticesByTitleOrderByViewCount(
      @Param("title") String title, @Param("authors") List<String> authors, Pageable pageable);

  // 좋아요 수 기준 정렬
  @Query(
      value =
          "SELECT * FROM SkuNotice s LEFT JOIN s.likes l WHERE s.author IN (:authors) "
              + "AND (:title IS NULL OR :title = '' OR s.title LIKE CONCAT('%', :title, '%')) "
              + "GROUP BY s ORDER BY s.date DESC, COUNT(l) DESC",
      nativeQuery = true)
  List<SkuNotice> findAllECNoticesByTitleOrderByLikeCount(
      @Param("title") String title, @Param("authors") List<String> authors, Pageable pageable);
}
