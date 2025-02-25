package org.example.skuhomepage.domain.calendar.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
public class GoogleCalendarResponseDTO {

  private String kind;
  private String etag;
  private String description;
  private String updated;
  private String timeZone;
  private String accessRole;
  private List<String> defaultReminders;
  private String nextPageToken;
  private List<Item> items;

  @ToString
  @Getter
  public static class Item {
    private String kind;
    private String etag;
    private String id;
    private String status;
    private String htmlLink;
    private String created;
    private String updated;
    private String summary;
    private Creator creator;
    private Organizer organizer;
    private ScheduleDate start;
    private ScheduleDate end;
    private String transparency;
    private String iCalUID;
    private int sequence;
    private boolean guestsCanModify;
    private String eventType;
  }

  private static class Creator {
    private String email;
  }

  private static class Organizer {
    private String email;
    private String displayName;
    private String self;
  }

  @Getter
  public static class ScheduleDate {
    private String date;
  }
}
