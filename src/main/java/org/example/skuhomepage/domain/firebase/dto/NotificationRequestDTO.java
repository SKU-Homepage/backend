package org.example.skuhomepage.domain.firebase.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class NotificationRequestDTO {
  @Getter
  @AllArgsConstructor
  public static class deleteAlarmDTO {
    private List<Long> alarmIds;
  }
}
