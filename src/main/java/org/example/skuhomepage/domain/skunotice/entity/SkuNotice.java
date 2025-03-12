package org.example.skuhomepage.domain.skunotice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SkuNotice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private LocalDateTime date;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private int view_count;

  @Column(nullable = false)
  private String image;

  @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
  private List<Likes> likes = new ArrayList<>();

}
