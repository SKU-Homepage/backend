package org.example.skuhomepage.domain.firebase.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.firebase.dto.NotificationResponseDTO;
import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.springframework.security.core.userdetails.UserDetails;
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
  private final AlarmRepository alarmRepository;

  public void sendPush(String token, String title, String body, String redirectPath) {
    Message message =
        Message.builder()
            .setToken(token)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .putData("redirect", redirectPath)
            .build();

    try {
      String response = firebaseMessaging.send(message);
      System.out.println("푸시 알림이 전송되었습니다. 토큰: " + token + ": " + response);
    } catch (FirebaseMessagingException e) {
      System.err.println("푸시 알림 전송 실패: " + token + " - " + e.getMessage());
    }
  }

  public NotificationResponseDTO.NotificationListDTO getNotificationList(UserDetails userDetails) {

    List<Alarm> alarms =
        alarmRepository.findAllByUser_AccountOrderByCreatedAtDesc(userDetails.getUsername());

    List<NotificationResponseDTO.NotificationDTO> notificationDTOList =
        alarms.stream()
            .map(
                notification ->
                    new NotificationResponseDTO.NotificationDTO(
                        notification.getTitle(), notification.getNotificationType().name()))
            .collect(Collectors.toList());

    return new NotificationResponseDTO.NotificationListDTO(notificationDTOList);
  }
}
