package org.example.skuhomepage.domain.firebase.consumer;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

// 이 클래스를 Spring Bean으로 등록합니다.
@Component
@Slf4j
public class JobTracker {
  private AtomicInteger totalJobs = new AtomicInteger(0);
  private AtomicInteger completedJobs = new AtomicInteger(0);
  private long startTime = 0;

  public void start(int total) {
    this.totalJobs.set(total);
    this.completedJobs.set(0);
    this.startTime = System.currentTimeMillis();
    log.info("전체 비동기 작업 시작. 총 {}개 메시지.", total);
  }

  public void completeOne() {
    int completed = completedJobs.incrementAndGet();
    if (completed >= totalJobs.get()) {
      long duration = System.currentTimeMillis() - startTime;
      log.info("====== 모든 비동기 작업 완료! 총 소요 시간: {} ms ======", duration);
    }
  }
}
