package org.example.skuhomepage.domain.timetable.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    @Query("SELECT t FROM TimeTable t WHERE t.user.account = :account")
    Optional<TimeTable> findByUser_Account(@Param("account") String account);
}
