package org.example.skuhomepage.domain.firebase.entity;

import jakarta.persistence.*;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.global.common.BaseTimeEntity;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"keyword", "topicGroup"})})
public class UserKeyword extends BaseTimeEntity {
  @Id @GeneratedValue private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @Column(nullable = false)
  private String keyword;

  //  @Column(nullable = false)
  //  private TopicGroup topicGroup;
}
