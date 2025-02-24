package org.example.skuhomepage.global.listener;

import org.example.skuhomepage.global.utils.FirebaseUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RedisExpireListener implements MessageListener {

  private final FirebaseUtils firebaseUtils;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String expiredKey = message.toString();

    if (expiredKey.contains(":token:")) {
      String token = expiredKey.split("token:")[1];
      System.out.println("Token expired: " + token);

      firebaseUtils.unsubscribeAllTopicByToken(token);
    }
  }
}
