package org.example.skuhomepage.domain.timetable.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.example.skuhomepage.global.common.BaseTimeEntity;
import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 유저랑 매핑 필요
// entity는 에브리타임 과목이랑 동일하게 임시로 설정 후 크롤링 후 수정 예정
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Subject extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("과목명")
  @Column(nullable = false)
  private String subject;

  @Comment("수업 시간")
  @Column(nullable = false)
  private String time;

  @Comment("담당 교수")
  @Column(nullable = false)
  private String professor;

  @Comment("강의실")
  @Column(nullable = false)
  private String classroom;

  @Comment("학점")
  @Column(nullable = false)
  private int credit;

  @Comment("학년")
  @Column(nullable = false)
  private int grade;

  @Comment("수강대상")
  @Column(nullable = false)
  private String target;

  @Comment("구분")
  @Enumerated(EnumType.STRING)
  private SubjectType division;
}
