package org.example.skuhomepage.global.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequest {

  @NotBlank private String title;
  @NotBlank private String content;
  private String contentUrl;
  private String inAppLink;
  @NotNull private LocalDateTime sendTime;
  private String token;
  private String topic;

  public Message toMessage() {

    Notification notification = Notification.builder().setTitle(title).setBody(content).build();

    Message.Builder messageBuilder =
        Message.builder()
            .putData("title", title)
            .putData("content", content)
            .putData("contentUrl", contentUrl != null ? contentUrl : "")
            .putData("inAppLink", inAppLink != null ? inAppLink : "")
            .putData("sendTime", sendTime.toString())
            .setNotification(notification);

    if (this.getTopic() != null && !this.getTopic().isBlank()) {
      messageBuilder.setTopic(this.getTopic());
    } else if (this.getToken() != null && !this.getToken().isBlank()) {
      messageBuilder.setToken(this.getToken());
    } else {
      throw new RuntimeException("토픽 또는 토큰을 지정해주세요.");
    }

    return messageBuilder.build();
  }
}
