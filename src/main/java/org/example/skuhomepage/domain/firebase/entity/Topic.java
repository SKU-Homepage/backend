package org.example.skuhomepage.domain.firebase.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"topic", "topicGroup"})})
public class Topic {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String topic;

  @Column(nullable = false)
  private String topicGroup;
}
