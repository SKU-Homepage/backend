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
@Setter
public class Alarm extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alarm_seq_generator")
  @SequenceGenerator(name = "alarm_seq_generator", sequenceName = "ALARM_SEQ", allocationSize = 50)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private User user;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 500)
  private String content;

  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  private String redirectUrl;

  @Column(name = "is_read")
  private boolean read;
}
