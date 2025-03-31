package org.example.skuhomepage.domain.calendar.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.example.skuhomepage.domain.mypage.entity.User;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private boolean isAllDay;

  @Column(nullable = false)
  private LocalDateTime startDateTime;

  @Column(nullable = false)
  private LocalDateTime endDateTime;

  @Column(nullable = false)
  private String labelColor;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public boolean getIsAllDay() {
    return isAllDay;
  }
}
