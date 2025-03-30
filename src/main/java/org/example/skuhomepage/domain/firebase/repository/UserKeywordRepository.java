package org.example.skuhomepage.domain.firebase.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.firebase.entity.UserKeyword;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {
  boolean existsByUserAndKeyword(User user, String keyword);

  List<UserKeyword> findAllByUser(User user);

  Optional<UserKeyword> findByUserAndKeyword(User user, String keyword);
}
