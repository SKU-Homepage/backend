package org.example.skuhomepage;

import org.example.skuhomepage.domain.calendar.service.UserScheduleService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRabbit
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class SkuHomepageApplication {
  public static void main(String[] args) {
    SpringApplication.run(SkuHomepageApplication.class, args);
  }

  @Bean
  public ApplicationRunner runner(UserScheduleService userScheduleService) {
    return args -> {
      System.out.println("====== 애플리케이션 시작: sendDailyUserSchedulePush() 호출 시도 ======");
      userScheduleService.sendDailyUserSchedulePush();
      System.out.println("====== sendDailyUserSchedulePush() 호출 완료 ======");
    };
  }
}
