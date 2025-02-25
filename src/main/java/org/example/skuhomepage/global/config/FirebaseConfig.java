package org.example.skuhomepage.global.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfig {

  @Value("${firebase.service-account-file.path}")
  private String keyFileSrc;

  @PostConstruct
  public void initialize() {
    try {

      ClassPathResource keyFile = new ClassPathResource(keyFileSrc);

      InputStream serviceAccount = keyFile.getInputStream();
      FirebaseOptions options =
          FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("Firebase Admin SDK 초기화가 완료되었습니다.");
      }

    } catch (FileNotFoundException e) {
      log.error("Firebase Admin SDK 초기화 중 인증 파일을 찾지 못하였습니다.", e);
    } catch (IOException e) {
      log.error("Firebase Admin SDK 초기화 중 오류가 발생했습니다.", e);
    }
  }
}
