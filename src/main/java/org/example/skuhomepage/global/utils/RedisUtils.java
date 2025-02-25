package org.example.skuhomepage.global.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisUtils {

  private final RedisTemplate<String, Object> redisTemplate;

  public void saveTokenWithExpiry(Long userId, String token, long ttl) {

    String userTokenKey = userId + ":tokens";

    redisTemplate.opsForSet().add(userTokenKey, token);

    String tokenKey = userId + ":token:" + token;
    redisTemplate.opsForValue().set(tokenKey, token, ttl, TimeUnit.SECONDS);
  }

  public Set<String> getTokens(long userId) {
    String userTokenKey = userId + ":tokens";
    Set<Object> tokens = redisTemplate.opsForSet().members(userTokenKey);

    Set<String> stringTokens = new HashSet<>();
    for (Object token : tokens) {
      stringTokens.add((String) token);
    }

    return stringTokens;
  }

  public void saveValue(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public String getValue(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }
}
