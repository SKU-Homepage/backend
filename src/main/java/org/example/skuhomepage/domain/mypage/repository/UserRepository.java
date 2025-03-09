package org.example.skuhomepage.domain.mypage.repository;

import java.util.Optional;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAccount(String account);

  boolean existsByStudentNumber(String studentNumber);
}
