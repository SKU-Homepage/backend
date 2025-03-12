package org.example.skuhomepage.domain.skunotice.enums;

import lombok.Getter;

@Getter
public enum ECNoticeType {
  ALL(""),
  GYOSU_HAKSEUB("교수학습원"),
  JINLO_CHWIEOB("진로취업지원센터"),
  DAEHAK_HYEOKSIN("대학혁신지원사업단"),
  ;

  private final String value;

  ECNoticeType(String value) {
    this.value = value;
  }
}
