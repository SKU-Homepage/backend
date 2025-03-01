package org.example.skuhomepage.domain.mypage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.example.skuhomepage.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String college;

  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private String major;

  @Column(nullable = false)
  private String studentNumber;

  @Column(nullable = false)
  private String grade;

  @Column(nullable = false)
  private String account;

  @Column(nullable = false)
  private boolean agreement;

  public void updateUserInfo(
      String college,
      String department,
      String major,
      String studentNumber,
      String grade,
      boolean agreement) {
    this.college = college;
    this.department = department;
    this.major = major;
    this.studentNumber = studentNumber;
    this.grade = grade;
    this.agreement = agreement;
  }
}
