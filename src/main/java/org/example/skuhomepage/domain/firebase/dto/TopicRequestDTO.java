package org.example.skuhomepage.domain.firebase.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicRequestDTO {

  @NotBlank private String keyword;
}
