package org.example.skuhomepage.domain.timetable.entity.mapping;

import jakarta.persistence.*;

import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.example.skuhomepage.domain.timetable.entity.TimeTable;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TimeTableSubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "timetable_id", nullable = false)
  private TimeTable timeTable;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_id", nullable = false)
  private Subject subject;

  @Column(nullable = false)
  private boolean isCustomSubject;
}
