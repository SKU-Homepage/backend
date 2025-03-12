package org.example.skuhomepage.domain.skunotice.repository;

import java.util.List;

import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkuNoticeRepository extends JpaRepository<SkuNotice, Long> {

  // 날짜 기준 정렬
  @Query(
      value =
          "SELECT * FROM sku_notice s WHERE s.author IN (:authors) "
              + "AND (:title IS NULL OR :title = '' OR s.title LIKE CONCAT('%', :title, '%')) ",
      nativeQuery = true)
  List<SkuNotice> findAllECNoticesByTitleOrderByDate(
      @Param("title") String title, @Param("authors") List<String> authors, Pageable pageable);

  // 조회수 기준 정렬
  @Query(
      value =
          "SELECT * FROM sku_notice s WHERE s.author IN (:authors) "
              + "AND (:title IS NULL OR :title = '' OR s.title LIKE CONCAT('%', :title, '%')) ",
      nativeQuery = true)
  List<SkuNotice> findAllECNoticesByTitleOrderByViewCount(
      @Param("title") String title, @Param("authors") List<String> authors, Pageable pageable);

  // 좋아요 수 기준 정렬
  @Query(
      value =
          "SELECT s.*, COUNT(l.id) as like_count FROM sku_notice s LEFT JOIN likes l ON s.id = l.notice_id "
              + "WHERE s.author IN (:authors) "
              + "AND (:title IS NULL OR :title = '' OR s.title LIKE CONCAT('%', :title, '%')) "
              + "GROUP BY s.id",
      nativeQuery = true)
  List<SkuNotice> findAllECNoticesByTitleOrderByLikeCount(
      @Param("title") String title, @Param("authors") List<String> authors, Pageable pageable);

  // 공지사항 검색
  @Query("SELECT n FROM SkuNotice n WHERE n.title LIKE %:keyword%")
  Page<SkuNotice> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
