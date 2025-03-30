package org.example.skuhomepage.domain.timetable.repository;

import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
  @Query("select s from Subject s where s.subject like %:name%")
  Page<Subject> findBySubjectName(String name, Pageable pageable);
}
