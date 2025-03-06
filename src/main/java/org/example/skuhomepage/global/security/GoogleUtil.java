package org.example.skuhomepage.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleUtil {

  @Value("${google.client-id}")
  private String clientId;

  @Value("${google.client-secret}")
  private String clientSecret;

  @Value("${google.local-redirect-uri}") // 로컬용 리디렉트 URI
  private String localRedirectUri;

  @Value("${google.deploy-redirect-uri}") // 배포용 리디렉트 URI
  private String deployRedirectUri;

  private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
  private static final String GOOGLE_USER_INFO_URL =
      "https://www.googleapis.com/oauth2/v3/userinfo";

  private final RestTemplate restTemplate = new RestTemplate();

  public String getRedirectUri(int env) {
    if (env == 0) {
      return localRedirectUri;
    } else if (env == 1) {
      return deployRedirectUri;
    } else {
      throw new IllegalArgumentException("잘못된 환경 값입니다. (0: 로컬, 1: 배포)");
    }
  }

  public GoogleDTO.OAuthToken requestToken(String authorizationCode, int env) {
    String redirectUri = getRedirectUri(env);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", authorizationCode);
    params.add("client_id", clientId);
    params.add("client_secret", clientSecret);
    params.add("redirect_uri", redirectUri);
    params.add("grant_type", "authorization_code");

    log.info(
        "요청된 params: code={}, client_id={}, redirect_uri={}",
        authorizationCode,
        clientId,
        redirectUri);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(GOOGLE_TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
    log.info(response.getBody());
    try {

      log.info("구글 토큰 값: {}", response.getBody());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.getBody(), GoogleDTO.OAuthToken.class);
    } catch (Exception e) {

      log.error(
          "구글 액세스 토큰 요청 실패 - code: {}, redirect_uri: {}, error: {}",
          authorizationCode,
          redirectUri,
          e.getMessage());

      throw new RuntimeException("구글 액세스 토큰 요청 실패");
    }
  }

  public GoogleDTO.UserInfo requestUserInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Bearer " + accessToken);

    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    try {
      ResponseEntity<String> response =
          restTemplate.exchange(GOOGLE_USER_INFO_URL, HttpMethod.GET, requestEntity, String.class);

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.getBody(), GoogleDTO.UserInfo.class);
    } catch (Exception e) {
      log.error("구글 사용자 정보 요청 실패", e);
      throw new RuntimeException("구글 사용자 정보 요청 실패");
    }
  }
}
