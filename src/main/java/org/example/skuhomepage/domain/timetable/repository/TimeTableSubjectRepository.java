package org.example.skuhomepage.domain.timetable.repository;

import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.example.skuhomepage.domain.timetable.entity.mapping.TimeTableSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableSubjectRepository extends JpaRepository<TimeTableSubject, Long> {
    boolean existsByTimeTableAndSubject(TimeTable timeTable, Subject subject);

}
