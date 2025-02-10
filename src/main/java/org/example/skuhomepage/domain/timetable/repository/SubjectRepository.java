package org.example.skuhomepage.domain.timetable.repository;

import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {}
