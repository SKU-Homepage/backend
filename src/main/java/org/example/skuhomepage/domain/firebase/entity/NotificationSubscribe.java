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
@Table(
    indexes = {@Index(name = "idx_topic", columnList = "topic")},
    uniqueConstraints = {@UniqueConstraint(columnNames = {"topic", "token"})})
public class NotificationSubscribe extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String topic;

  @Column(nullable = false)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
