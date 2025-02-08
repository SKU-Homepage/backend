package org.example.skuhomepage.domain.timetable.repository;

import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {}
