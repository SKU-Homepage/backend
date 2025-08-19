package org.example.skuhomepage.domain.mypage.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAccount(String account);

  boolean existsByStudentNumber(String studentNumber);

  @Query("SELECT u FROM User u JOIN FETCH u.userDeviceTokens WHERE u.id IN :userIds")
  List<User> findUsersWithDeviceTokensByIds(@Param("userIds") List<Long> userIds);
}
