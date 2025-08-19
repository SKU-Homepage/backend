package org.example.skuhomepage.domain.calendar.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.example.skuhomepage.domain.calendar.dto.DateTimeDTO;
import org.example.skuhomepage.domain.calendar.dto.ScheduleResponseDTO;
import org.example.skuhomepage.domain.calendar.dto.SkuCalendarResponseDTO;
import org.example.skuhomepage.domain.calendar.dto.UserScheduleRequestDTO.*;
import org.example.skuhomepage.domain.calendar.dto.UserScheduleResponseDTO.UserScheduleDTO;
import org.example.skuhomepage.domain.calendar.entity.UserSchedule;
import org.example.skuhomepage.domain.calendar.exception.CalendarErrorStatus;
import org.example.skuhomepage.domain.calendar.repository.UserScheduleRepository;
import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.example.skuhomepage.domain.firebase.repository.UserDeviceTokenRepository;
import org.example.skuhomepage.domain.firebase.service.NotificationService;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserScheduleService {

  private final UserScheduleRepository userScheduleRepository;
  private final UserRepository userRepository;
  private final SkuCalendarService skuCalendarService;
  private final UserDeviceTokenRepository userDeviceTokenRepository;
  private final AlarmRepository alarmRepository;
  private final NotificationService notificationService;

  public List<UserScheduleDTO> getUserSchedule(int year, int month, int day, long userId) {

    List<UserSchedule> userScheduleList = getUserScheduleList(year, month, day, userId);

    List<UserScheduleDTO> userScheduleDTOList = new ArrayList<>();
    for (UserSchedule userSchedule : userScheduleList) {
      userScheduleDTOList.add(new UserScheduleDTO(userSchedule));
    }

    return userScheduleDTOList;
  }

  public long addUserSchedule(AddUserScheduleDTO requestDTO, long userId) {

    if (requestDTO.isAllDay()) {
      LocalDateTime start = requestDTO.getStart().date().atStartOfDay();
      LocalDateTime end = requestDTO.getEnd().date().plusDays(1).atStartOfDay().minusNanos(1000);

      requestDTO.setStart(DateTimeDTO.of(start));
      requestDTO.setEnd(DateTimeDTO.of(end));
    }

    validateScheduleDates(requestDTO.getStart(), requestDTO.getEnd());

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    return userScheduleRepository.save(AddUserScheduleDTO.toEntity(requestDTO, user)).getId();
  }

  public UserScheduleDTO updateUserSchedule(
      long scheduleId, UpdateUserScheduleDTO requestDTO, long userId) {

    return userScheduleRepository
        .findById(scheduleId)
        .filter(userSchedule -> userSchedule.getUser().getId() == userId)
        .map(
            userSchedule -> {
              LocalDateTime startDateTime =
                  (requestDTO.getAllDay() != null && requestDTO.getAllDay())
                      ? requestDTO.getStart().date().atStartOfDay()
                      : (requestDTO.getStart() != null
                          ? LocalDateTime.of(
                              requestDTO.getStart().date() != null
                                  ? requestDTO.getStart().date()
                                  : userSchedule.getStartDateTime().toLocalDate(),
                              requestDTO.getStart().time() != null
                                  ? requestDTO.getStart().time()
                                  : userSchedule.getStartDateTime().toLocalTime())
                          : userSchedule.getStartDateTime());

              LocalDateTime endDateTime =
                  (requestDTO.getAllDay() != null && requestDTO.getAllDay())
                      ? requestDTO.getEnd().date().plusDays(1).atStartOfDay().minusNanos(1000)
                      : (requestDTO.getEnd() != null
                          ? LocalDateTime.of(
                              requestDTO.getEnd().date() != null
                                  ? requestDTO.getEnd().date()
                                  : userSchedule.getEndDateTime().toLocalDate(),
                              requestDTO.getEnd().time() != null
                                  ? requestDTO.getEnd().time()
                                  : userSchedule.getEndDateTime().toLocalTime())
                          : userSchedule.getEndDateTime());

              UserSchedule updatedUserSchedule =
                  UserSchedule.builder()
                      .id(userSchedule.getId())
                      .title(
                          requestDTO.getTitle() != null
                              ? requestDTO.getTitle()
                              : userSchedule.getTitle())
                      .startDateTime(startDateTime)
                      .endDateTime(endDateTime)
                      .isAllDay(
                          requestDTO.getAllDay() != null
                              ? requestDTO.getAllDay()
                              : userSchedule.getIsAllDay())
                      .labelColor(
                          requestDTO.getLabelColor() != null
                              ? requestDTO.getLabelColor()
                              : userSchedule.getLabelColor())
                      .build();

              validateScheduleDates(
                  DateTimeDTO.of(updatedUserSchedule.getStartDateTime()),
                  DateTimeDTO.of(updatedUserSchedule.getEndDateTime()));

              return new UserScheduleDTO(userScheduleRepository.save(updatedUserSchedule));
            })
        .orElseThrow(() -> new GeneralException(CalendarErrorStatus.SCHEDULE_NOT_FOUND));
  }

  public void deleteUserSchedule(long scheduleId, long userId) {

    userScheduleRepository
        .findById(scheduleId)
        .ifPresentOrElse(
            schedule -> {
              if (schedule.getUser().getId() == userId) {
                userScheduleRepository.delete(schedule);
              } else {
                // 권한 없음: 삭제할 일정이 본인의 일정이 아님(보안 적인 이유로 NOT_FOUND로 처리)
                throw new GeneralException(CalendarErrorStatus.SCHEDULE_NOT_FOUND);
              }
            },
            () -> {
              throw new GeneralException(CalendarErrorStatus.SCHEDULE_NOT_FOUND);
            });
  }

  public List<ScheduleResponseDTO> getAllSchedule(int year, int month, int day, long userId) {
    List<UserScheduleDTO> userScheduleList = getUserSchedule(year, month, day, userId);
    List<SkuCalendarResponseDTO.SkuScheduleDTO> skuScheduleDTOList =
        skuCalendarService.getSkuCalendar(year, month, day);

    return ScheduleResponseDTO.toDtoList(userScheduleList, skuScheduleDTOList);
  }

  private List<UserSchedule> getUserScheduleList(int year, int month, int day, long userId) {
    int lastDay = YearMonth.of(year, month).lengthOfMonth();
    LocalDateTime startDateTime = LocalDateTime.of(year, month, day == 0 ? 1 : day, 0, 0, 0);
    LocalDateTime endDateTime =
        LocalDateTime.of(year, month, day == 0 ? lastDay : day, 0, 0).plusDays(1).minusNanos(1000);

    System.out.println("startDateTime: " + startDateTime);
    System.out.println("endDateTime: " + endDateTime);

    return day == 0
        ? userScheduleRepository.findSchedulesByUserAndMonth(userId, startDateTime, endDateTime)
        : userScheduleRepository.findSchedulesByUserAndDay(userId, startDateTime);
  }

  private void validateScheduleDates(DateTimeDTO start, DateTimeDTO end) {

    if (start.isValidDate() || end.isValidDate()) {
      throw new GeneralException(CalendarErrorStatus.SCHEDULE_TIME_CANNOT_NULL);
    }
    if (start.isAfter(end)) {
      throw new GeneralException(CalendarErrorStatus.SCHEDULE_DATE_RANGE_BAD_REQUEST);
    }
  }

  // @Scheduled(cron = "0 0 8 * * ?") // 매일 오전 8시에 실행
  @Transactional
  public void sendDailyUserSchedulePush_Sync() {
    StopWatch stopWatch = new StopWatch("Sync Push Notification");
    log.info("동기 방식 푸시 알림 전송을 시작합니다...");
    stopWatch.start("1. 데이터 조회");

    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

    // 1. 먼저 조건에 맞는 User와 Schedule을 조회
    List<User> usersWithSchedules =
        userRepository.findUsersWithSchedulesForDate(startOfDay, endOfDay);

    if (usersWithSchedules.isEmpty()) {
      stopWatch.stop();
      log.info("알림을 보낼 사용자가 없어 작업을 종료합니다. 소요 시간: {} ms", stopWatch.getTotalTimeMillis());
      return;
    }

    // 2. 위에서 찾은 User들의 Token 정보를 추가로 조회 (IN 절 사용)
    List<User> usersWithSchedulesAndTokens = userRepository.findUsersWithTokens(usersWithSchedules);
    log.info("오늘 일정이 있는 사용자 {}명의 데이터를 모두 조회했습니다.", usersWithSchedulesAndTokens.size());
    stopWatch.stop();

    stopWatch.start("2. 알림 전송 및 저장 처리");
    List<Alarm> alarmsToSave = new ArrayList<>();

    // 이제 usersWithSchedulesAndTokens 리스트는 필요한 모든 정보를 가지고 있습니다.
    for (User user : usersWithSchedulesAndTokens) {

      String pushTitle = "오늘의 일정";
      String pushBody =
          user.getSchedules().stream()
              .map(
                  sch ->
                      sch.getTitle()
                          + " ("
                          + (sch.getIsAllDay()
                              ? "하루 종일"
                              : sch.getStartDateTime().toLocalTime()
                                  + " ~ "
                                  + sch.getEndDateTime().toLocalTime())
                          + ")")
              .collect(Collectors.joining(", "));

      for (UserDeviceToken token : user.getUserDeviceTokens()) {
        notificationService.sendPush(token.getFcmToken(), pushTitle, pushBody, "/");
      }
    }

    //      Alarm alarm =
    //              Alarm.builder()
    //                      .user(user)
    //                      .title(pushTitle)
    //                      .content(pushBody)
    //                      .notificationType(NotificationType.CALENDAR)
    //                      .build();
    //      alarmsToSave.add(alarm);
    //    }
    //
    //    // 4. 모든 알림을 DB에 한 번에 저장 (배치 INSERT)
    //    alarmRepository.saveAll(alarmsToSave);

    stopWatch.stop(); // <-- 전체 측정을 위해 여기서 한 번만 멈춥니다.
    log.info("동기 방식 푸시 알림 전송 완료. 총 소요 시간: {} ms", stopWatch.getTotalTimeMillis());
    // prettyPrint()는 여러 작업을 측정했을 때 유용하므로, 여기서는 getTotalTimeMillis()만 사용해도 충분합니다.
    // log.info(stopWatch.prettyPrint());
  }
}
