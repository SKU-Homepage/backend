package org.example.skuhomepage.domain.firebase.entity;

public enum NotificationType {
  NOTICE,
  EC_NOTICE,
  TIMETABLE,
  CALENDAR;

  public String getRedirectUrl() {
    return switch (this) {
      case NOTICE, EC_NOTICE -> "/notice";
      case CALENDAR -> "/calendar";
      case TIMETABLE -> "/schedule";
    };
  }
}
