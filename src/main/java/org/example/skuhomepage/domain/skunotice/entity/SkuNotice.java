package org.example.skuhomepage.domain.skunotice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SkuNotice {

  @Id private Long id;

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
  @Setter
  private int viewCount;

  @Column(columnDefinition = "TEXT")
  private String image;

  @Builder.Default
  @OneToMany(mappedBy = "skuNotice", cascade = CascadeType.ALL)
  private List<Likes> likes = new ArrayList<>();
}
