package org.example.skuhomepage.domain.calendar.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.example.skuhomepage.domain.calendar.entity.UserSchedule;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {

  @Query(
      "SELECT us FROM UserSchedule us "
          + "WHERE us.user.id = :userId "
          + "AND (:startDayOfMonth <= us.startDateTime AND us.startDateTime <= :endDayOfMonth) "
          + "OR (:startDayOfMonth <= us.endDateTime AND us.endDateTime <= :endDayOfMonth) "
          + "ORDER BY us.startDateTime ASC")
  List<UserSchedule> findSchedulesByUserAndMonth(
      long userId, LocalDateTime startDayOfMonth, LocalDateTime endDayOfMonth);

  @Query(
      "SELECT us FROM UserSchedule us "
          + "WHERE us.user.id = :userId "
          + "AND us.startDateTime <= :day AND us.endDateTime >= :day "
          + "ORDER BY us.startDateTime ASC")
  List<UserSchedule> findSchedulesByUserAndDay(long userId, LocalDateTime day);

  List<UserSchedule> findAllByUserAndStartDateTimeBetween(
      User user, LocalDateTime start, LocalDateTime end);
}
