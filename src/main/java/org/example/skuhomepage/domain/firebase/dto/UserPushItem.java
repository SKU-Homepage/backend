package org.example.skuhomepage.domain.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPushItem {
  private Long userId;
  private String title;
  private String body;
}
