// package org.example.skuhomepage.domain.firebase.controller;
//
// import org.example.skuhomepage.domain.calendar.service.UserScheduleService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/batch") // API 경로의 공통 부분
// @RequiredArgsConstructor
// public class BatchController {
//
//  private final UserScheduleService userScheduleService;
//
//  // POST http://localhost:8080/batch/send-notifications 요청을 받으면 실행됩니다.
//  @PostMapping("/alarmTest")
//  public ResponseEntity<String> startDailyPushJob() {
//    System.out.println("====== 컨트롤러 경유: sendDailyUserSchedulePush() 호출 시도 ======");
//    // 서비스 메서드를 여기서 호출합니다.
//    userScheduleService.sendDailyUserSchedulePush();
//    System.out.println("====== sendDailyUserSchedulePush() 호출 완료 ======");
//
//    // API 호출자에게 성공적으로 시작되었음을 알리는 응답을 보냅니다.
//    return ResponseEntity.ok("Push notification job started successfully.");
//  }
// }
