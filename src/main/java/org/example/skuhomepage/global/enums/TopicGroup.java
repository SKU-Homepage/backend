package org.example.skuhomepage.global.enums;

import lombok.Getter;

@Getter
public enum TopicGroup {
  SKU_NOTICE("skunotice"), // 학사 공지
  SKU_EVENT("skuevent"), // 비교과 공지
  TIMETABLE("timetable"), // 시간표
  COMMON("common"), // 공통
  ;

  private final String value;

  TopicGroup(String value) {
    this.value = value;
  }
}
