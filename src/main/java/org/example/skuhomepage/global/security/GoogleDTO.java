package org.example.skuhomepage.global.security;

import lombok.Getter;

public class GoogleDTO {

  @Getter
  public static class OAuthToken {
    String access_token;
    String token_type;
    String refresh_token;
    Long expires_in;
    String scope;
    String id_token;
  }

  @Getter
  public static class UserInfo {
    String sub;
    String email;
    Boolean email_verified;
    String name;
    String given_name;
    String family_name;
    String picture;
    String locale;
    String hd;
  }
}
