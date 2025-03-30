package org.example.skuhomepage.domain.firebase.repository;

import java.util.List;

import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
  @Query("SELECT n FROM Alarm n WHERE n.user.account = :account ORDER BY n.createdDate DESC")
  List<Alarm> findAllByUser_AccountOrderByCreatedAtDesc(String account);
}
