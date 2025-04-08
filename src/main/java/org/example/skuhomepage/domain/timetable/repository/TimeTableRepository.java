package org.example.skuhomepage.domain.timetable.repository;

import java.util.Optional;

import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
  @Query("SELECT t FROM TimeTable t WHERE t.user.account = :account")
  Optional<TimeTable> findByUser_Account(@Param("account") String account);

  @Query(
      """
    SELECT t FROM TimeTable t
    JOIN FETCH t.timeTableSubjects ts
    JOIN FETCH ts.subject s
    WHERE t.user.account = :account
""")
  Optional<TimeTable> findByUserAccountWithSubjects(@Param("account") String account);
}
