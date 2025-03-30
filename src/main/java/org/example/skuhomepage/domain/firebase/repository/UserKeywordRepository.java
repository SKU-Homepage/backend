package org.example.skuhomepage.domain.firebase.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.firebase.entity.UserKeyword;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {
  boolean existsByUserAndKeyword(User user, String keyword);

  List<UserKeyword> findAllByUser(User user);

  @Query("SELECT us FROM UserKeyword us WHERE us.user = :user AND us.id = :keywordId")
  Optional<UserKeyword> findByUserAndKeywordId(
      @Param("user") User user, @Param("keywordId") Long keywordId);
}
