package org.example.skuhomepage.domain.firebase.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
  boolean existsByFcmToken(String fcmToken);

  Optional<UserDeviceToken> findByFcmToken(String fcmToken);

  List<UserDeviceToken> findAllByUser(User user);
}
