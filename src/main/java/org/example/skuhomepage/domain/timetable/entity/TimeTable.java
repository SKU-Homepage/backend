package org.example.skuhomepage.domain.timetable.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.timetable.entity.mapping.TimeTableSubject;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

// 학기별 시간표를 생성하기 위해 time table 엔티티 생성
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("시간표 이름")
    @Column(nullable = false)
    private String name;

    @Comment("사용자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTableSubject> timeTableSubjects = new ArrayList<>();
}
