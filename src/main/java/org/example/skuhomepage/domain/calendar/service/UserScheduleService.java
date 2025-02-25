package org.example.skuhomepage.domain.calendar.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.example.skuhomepage.domain.calendar.dto.DateTimeDTO;
import org.example.skuhomepage.domain.calendar.dto.UserScheduleRequestDTO.*;
import org.example.skuhomepage.domain.calendar.dto.UserScheduleResponseDTO.UserScheduleDTO;
import org.example.skuhomepage.domain.calendar.entity.UserSchedule;
import org.example.skuhomepage.domain.calendar.exception.CalendarErrorStatus;
import org.example.skuhomepage.domain.calendar.repository.UserScheduleRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserScheduleService {

  private final UserScheduleRepository userScheduleRepository;

  public List<UserScheduleDTO> getUserSchedule(int year, int month, int day) {

    int lastDay = YearMonth.of(year, month).lengthOfMonth();
    LocalDateTime startDateTime = LocalDateTime.of(year, month, day == 0 ? 1 : day, 0, 0, 0);
    LocalDateTime endDateTime =
        LocalDateTime.of(year, month, day == 0 ? lastDay : day, 0, 0).plusDays(1).minusNanos(1000);

    System.out.println("startDateTime: " + startDateTime);
    System.out.println("endDateTime: " + endDateTime);

    List<UserSchedule> userScheduleList =
        day == 0
            ? userScheduleRepository.findSchedulesByMonth(startDateTime, endDateTime)
            : userScheduleRepository.findSchedulesByDay(startDateTime);

    List<UserScheduleDTO> userScheduleDTOList = new ArrayList<>();
    for (UserSchedule userSchedule : userScheduleList) {
      userScheduleDTOList.add(new UserScheduleDTO(userSchedule));
    }

    return userScheduleDTOList;
  }

  public long addUserSchedule(AddUserScheduleDTO requestDTO) {

    if (requestDTO.isAllDay()) {
      LocalDateTime start = requestDTO.getStart().date().atStartOfDay();
      LocalDateTime end = requestDTO.getEnd().date().plusDays(1).atStartOfDay().minusNanos(1000);

      requestDTO.setStart(DateTimeDTO.of(start));
      requestDTO.setEnd(DateTimeDTO.of(end));
    }

    validateScheduleDates(requestDTO.getStart(), requestDTO.getEnd());

    return userScheduleRepository.save(AddUserScheduleDTO.toEntity(requestDTO)).getId();
  }

  public UserScheduleDTO updateUserSchedule(long scheduleId, UpdateUserScheduleDTO requestDTO) {

    return userScheduleRepository
        .findById(scheduleId)
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

  public void deleteUserSchedule(long scheduleId) {

    userScheduleRepository
        .findById(scheduleId)
        .ifPresentOrElse(
            userScheduleRepository::delete,
            () -> {
              throw new GeneralException(CalendarErrorStatus.SCHEDULE_NOT_FOUND);
            });
  }

  private void validateScheduleDates(DateTimeDTO start, DateTimeDTO end) {

    if (start.isValidDate() || end.isValidDate()) {
      throw new GeneralException(CalendarErrorStatus.SCHEDULE_TIME_CANNOT_NULL);
    }
    if (start.isAfter(end)) {
      throw new GeneralException(CalendarErrorStatus.SCHEDULE_DATE_RANGE_BAD_REQUEST);
    }
  }
}
