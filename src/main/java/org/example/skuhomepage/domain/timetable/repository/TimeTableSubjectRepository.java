package org.example.skuhomepage.domain.timetable.repository;

import java.util.List;
import java.util.Optional;

import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.example.skuhomepage.domain.timetable.entity.mapping.TimeTableSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface TimeTableSubjectRepository extends JpaRepository<TimeTableSubject, Long> {
  boolean existsByTimeTableAndSubject(TimeTable timeTable, Subject subject);

  @Query(
      "SELECT ts FROM TimeTableSubject ts WHERE ts.timeTable.user.account = :account AND ts.subject.id = :subjectId")
  Optional<TimeTableSubject> findByUserAccountBySubjectId(
      @Param("account") String account, @Param("subjectId") Long subjectId);

  List<TimeTableSubject> findAllByTimeTable(TimeTable timeTable);
}
