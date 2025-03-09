package org.example.skuhomepage.global.security;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.example.skuhomepage.domain.mypage.dto.CustomUserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
  private final Key key;
  private final Key refreshKey;
  private final long accessTokenExpTime;
  private final long refreshTokenExpTime;

  public JwtUtil(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.refreshSecret}") String refreshSecret,
      @Value("${jwt.expiration_time}") String expirationTime,
      @Value("${jwt.refreshExpirationTime}") String refreshExpirationTime) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    this.accessTokenExpTime = Long.parseLong(expirationTime);
    this.refreshTokenExpTime = Long.parseLong(refreshExpirationTime);
  }

  // JWT 생성
  private String createToken(CustomUserInfoDto user, long expireTime) {
    Claims claims = Jwts.claims().setSubject(user.getUserId().toString());
    claims.put("userId", user.getUserId());
    claims.put("email", user.getAccount());

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(tokenValidity.toInstant()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // Jwt 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsopported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
    }
    return false;
  }

  // refreshToken 검증
  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsopported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
    }
    return false;
  }

  // Jwt Claims 추출
  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // refresh Token Claim 추출
  public Claims parseRefreshTokenClaims(String refreshToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(refreshKey)
          .build()
          .parseClaimsJws(refreshToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // refreshToken 만료까지 남은 시간 계산
  public long getRemainTime(String refreshToken) {
    Date expiration =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(refreshToken)
            .getBody()
            .getExpiration();
    return expiration.getTime() - System.currentTimeMillis();
  }

  // AccessToken 생성
  public String createAccessToken(CustomUserInfoDto user) {
    return createToken(user, accessTokenExpTime);
  }

  // public 메서드로 refreshToken 생성
  public String createRefreshToken(CustomUserInfoDto user) {
    return createToken(user, refreshTokenExpTime);
  }

  // token 에서 userId 추출
  public String getUserEmail(String token) {
    return parseClaims(token).get("email", String.class);
  }

  // refresh Token에서 email추출
  public String getUserEmailFromRefreshToken(String token) {
    return parseRefreshTokenClaims(token).get("email", String.class);
  }
}
