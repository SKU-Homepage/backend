package org.example.skuhomepage.global.config;

import org.example.skuhomepage.global.listener.RedisExpireListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {

  private final RedisExpireListener redisExpireListener;
  private final RedisConnectionFactory redisConnectionFactory;

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    return redisTemplate;
  }

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);
    container.addMessageListener(redisExpireListener, new ChannelTopic("__keyevent@0__:expired"));
    return container;
  }

  @Bean
  public CommandLineRunner startRedisListener(RedisMessageListenerContainer container) {
    return args -> {
      container.start();
      log.info("Redis Expire Listener 등록 완료");
    };
  }
}
