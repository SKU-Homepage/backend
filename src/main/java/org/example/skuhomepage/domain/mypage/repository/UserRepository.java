package org.example.skuhomepage.domain.mypage.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAccount(String account);

  boolean existsByStudentNumber(String studentNumber);

  @Query(
      "SELECT u FROM User u "
          + "JOIN FETCH u.schedules s "
          + "LEFT JOIN FETCH u.userDeviceTokens "
          + "WHERE s.startDateTime >= :startOfDay AND s.startDateTime < :endOfDay")
  List<User> findUsersWithSchedulesAndTokensForDate(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

  @Query(
      "SELECT DISTINCT u FROM User u "
          + "JOIN FETCH u.schedules s "
          + "WHERE s.startDateTime >= :startOfDay AND s.startDateTime < :endOfDay")
  List<User> findUsersWithSchedulesForDate(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

  // 2단계: 조회된 User들의 Token 정보를 가져온다.
  // LEFT JOIN FETCH를 사용하여 토큰이 없는 유저도 포함시킵니다.
  @Query(
      "SELECT DISTINCT u FROM User u "
          + "LEFT JOIN FETCH u.userDeviceTokens "
          + "WHERE u IN :users")
  List<User> findUsersWithTokens(@Param("users") List<User> users);
}
