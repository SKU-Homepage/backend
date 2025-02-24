package org.example.skuhomepage.domain.firebase.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private String content;

  private String contentUrl;

  private String inAppLink;

  private LocalDateTime sendTime;
}
