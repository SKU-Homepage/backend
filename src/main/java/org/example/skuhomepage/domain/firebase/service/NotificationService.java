package org.example.skuhomepage.domain.firebase.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final FirebaseMessaging firebaseMessaging;

  public void sendPush(String token, String title, String body) {
    Message message =
        Message.builder()
            .setToken(token)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .build();

    try {
      String response = firebaseMessaging.send(message);
      System.out.println("푸시 알림이 전송되었습니다. 토큰: " + token + ": " + response);
    } catch (FirebaseMessagingException e) {
      System.err.println("푸시 알림 전송 실패: " + token + " - " + e.getMessage());
    }
  }
}
