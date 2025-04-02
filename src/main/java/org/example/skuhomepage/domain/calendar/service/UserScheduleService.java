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
import org.example.skuhomepage.domain.firebase.entity.NotificationType;
import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.example.skuhomepage.domain.firebase.repository.UserDeviceTokenRepository;
import org.example.skuhomepage.domain.firebase.service.NotificationService;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

  @Scheduled(cron = "0 0 8 * * ?") // 매일 오전 8시에 실행
  @Transactional
  public void sendDailyUserSchedulePush() {

    List<User> allUsers = userRepository.findAll();

    for (User user : allUsers) {
      List<UserSchedule> todaySchedules =
          userScheduleRepository.findAllByUserAndStartDateTimeBetween(
              user, LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());

      if (todaySchedules.isEmpty()) continue;

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

      List<UserDeviceToken> tokens = userDeviceTokenRepository.findAllByUser(user);

      for (UserDeviceToken token : tokens) {
        notificationService.sendPush(token.getFcmToken(), pushTitle, pushBody, "/");

        Alarm alarm =
            Alarm.builder()
                .user(user)
                .title(pushTitle)
                .content(pushBody)
                .notificationType(NotificationType.CALENDAR)
                .build();

        alarmRepository.save(alarm);
      }
    }
  }
}
