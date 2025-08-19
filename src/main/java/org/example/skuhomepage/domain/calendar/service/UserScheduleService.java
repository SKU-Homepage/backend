package org.example.skuhomepage.domain.calendar.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.example.skuhomepage.domain.firebase.dto.PushBatchMessage;
import org.example.skuhomepage.domain.firebase.dto.UserPushItem;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserScheduleService {

  private final UserScheduleRepository userScheduleRepository;
  private final UserRepository userRepository;
  private final SkuCalendarService skuCalendarService;
  private final RabbitTemplate rabbitTemplate;

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
  public void sendDailyUserSchedulePush() {
    StopWatch stopWatch =
        new StopWatch("Daily Push Notification Job"); // StopWatch에 ID를 부여하면 로그 보기가 편합니다.

    // --- 1. 사용자 ID 조회 ---
    stopWatch.start("1. Fetching User IDs");
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
    log.info("오늘 날짜 범위 확인: {} 부터 {} 까지의 스케줄을 조회합니다.", startOfDay, endOfDay);
    List<Long> userIds =
        userScheduleRepository.findUserIdsHavingTodaySchedule(today); // 수정한 쿼리 메서드 사용
    stopWatch.stop();

    log.info("DB에서 조회된 사용자 ID 목록 (총 {}명)", userIds.size());
    if (userIds.isEmpty()) {
      log.warn("오늘 날짜({})에 해당하는 일정이 있는 사용자가 없습니다. 작업을 종료합니다.", today);
      log.info(stopWatch.prettyPrint()); // 최종 결과 출력
      return;
    }

    // --- 2. 스케줄 상세 정보 조회 ---
    stopWatch.start("2. Fetching Schedules");
    List<UserSchedule> allTodaySchedules =
        userScheduleRepository.findSchedulesByUsersInDateRange(userIds, startOfDay, endOfDay);
    stopWatch.stop();
    log.info("총 {}개의 관련 스케줄을 한번에 조회했습니다.", allTodaySchedules.size());

    if (allTodaySchedules.isEmpty()) {
      log.warn("사용자 ID는 조회되었으나, 해당 시간에 맞는 스케줄이 없습니다. 쿼리 조건을 다시 확인해주세요.");
      stopWatch.stop();
      log.info(stopWatch.prettyPrint());
      return;
    }

    // --- 3. 데이터 그룹화 ---
    stopWatch.start("3. Grouping Schedules by User");
    Map<Long, List<UserSchedule>> schedulesByUser =
        allTodaySchedules.stream()
            .collect(Collectors.groupingBy(schedule -> schedule.getUser().getId()));
    stopWatch.stop();
    log.info("{}명의 사용자에 대한 스케줄을 그룹화했습니다.", schedulesByUser.size());

    // --- 4. 메시지 생성 및 전송 ---
    stopWatch.start("4. Creating and Sending Messages to RabbitMQ");
    List<UserPushItem> batchItems = new ArrayList<>();
    int batchSize = 5000;

    for (Map.Entry<Long, List<UserSchedule>> entry : schedulesByUser.entrySet()) {
      Long userId = entry.getKey();
      List<UserSchedule> todaySchedules = entry.getValue();

      String pushTitle = "오늘의 일정";
      String pushBody =
          todaySchedules.stream()
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

      batchItems.add(new UserPushItem(userId, pushTitle, pushBody));
      if (batchItems.size() >= batchSize) {
        rabbitTemplate.convertAndSend(
            "push.queue", new PushBatchMessage(new ArrayList<>(batchItems)));
        batchItems.clear();
      }
    }

    if (!batchItems.isEmpty()) {
      rabbitTemplate.convertAndSend(
          "push.queue", new PushBatchMessage(new ArrayList<>(batchItems)));
    }
    stopWatch.stop();

    // --- 최종 결과 출력 ---
    log.info("전체 작업 완료. \n{}", stopWatch.prettyPrint());
  }
}
