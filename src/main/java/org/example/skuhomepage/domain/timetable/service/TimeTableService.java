package org.example.skuhomepage.domain.timetable.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.example.skuhomepage.domain.firebase.entity.NotificationType;
import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.example.skuhomepage.domain.firebase.repository.UserDeviceTokenRepository;
import org.example.skuhomepage.domain.firebase.service.NotificationService;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO.selfSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.AddSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.DeleteSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MyTimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableListDTO;
import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.example.skuhomepage.domain.timetable.entity.SubjectType;
import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.example.skuhomepage.domain.timetable.entity.mapping.TimeTableSubject;
import org.example.skuhomepage.domain.timetable.exception.TimeTableErrorStatus;
import org.example.skuhomepage.domain.timetable.repository.SubjectRepository;
import org.example.skuhomepage.domain.timetable.repository.TimeTableRepository;
import org.example.skuhomepage.domain.timetable.repository.TimeTableSubjectRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeTableService {
  private final SubjectRepository subjectRepository;
  private final TimeTableRepository timeTableRepository;
  private final TimeTableSubjectRepository timeTableSubjectRepository;
  private final UserRepository userRepository;
  private final UserDeviceTokenRepository userDeviceTokenRepository;
  private final NotificationService notificationService;
  private final AlarmRepository alarmRepository;

  public TimeTableResponseDTO.TodayTimeTableListDTO getTodayTimeTable(UserDetails userDetails) {
    DayOfWeek today = LocalDate.now().getDayOfWeek();

    TimeTable timeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.TIME_TABLE_NOT_FOUND));

    List<TimeTableResponseDTO.TimeTableDTO> todaySubjects =
        timeTable.getTimeTableSubjects().stream()
            .filter(ts -> isSubjectOnToday(ts.getSubject(), today))
            .map(
                ts ->
                    new TimeTableResponseDTO.TimeTableDTO(
                        ts.getSubject().getId(),
                        ts.getSubject().getSubject(),
                        ts.getSubject().getProfessor(),
                        ts.getSubject().getDay(),
                        ts.getSubject().getStartTime(),
                        ts.getSubject().getStartTime(),
                        ts.getSubject().getClassroom(),
                        ts.getSubject().getCredit(),
                        ts.getSubject().getGrade(),
                        ts.getSubject().getTarget(),
                        ts.getSubject().getDivision().name()))
            .collect(Collectors.toList());
    return new TimeTableResponseDTO.TodayTimeTableListDTO(todaySubjects);
  }

  public MyTimeTableDTO getMyTimeTable(UserDetails userDetails) {
    TimeTable timeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.TIMETABLE_NOT_FOUND));

    List<TimeTableResponseDTO.TimeTableDTO> subjects =
        timeTable.getTimeTableSubjects().stream()
            .map(
                ts ->
                    new TimeTableResponseDTO.TimeTableDTO(
                        ts.getSubject().getId(),
                        ts.getSubject().getSubject(),
                        ts.getSubject().getProfessor(),
                        ts.getSubject().getDay(),
                        ts.getSubject().getStartTime(),
                        ts.getSubject().getStartTime(),
                        ts.getSubject().getClassroom(),
                        ts.getSubject().getCredit(),
                        ts.getSubject().getGrade(),
                        ts.getSubject().getTarget(),
                        ts.getSubject().getDivision().name()))
            .collect(Collectors.toList());

    return new MyTimeTableDTO(subjects);
  }

  public TimeTableListDTO getTimeTableList(UserDetails userDetails, Pageable pageable) {
    Slice<Subject> subjectList = subjectRepository.findAll(pageable);

    List<TimeTableResponseDTO.TimeTableDTO> timeTableDTOS =
        subjectList.getContent().stream()
            .map(TimeTableResponseDTO.TimeTableDTO::new)
            .collect(Collectors.toList());

    return new TimeTableListDTO(timeTableDTOS, subjectList.hasNext(), pageable.getPageNumber() + 1);
  }

  public AddSubjectDTO addSubject(UserDetails userDetails, List<Long> subjectIds) {
    TimeTable timeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseGet(
                () -> {
                  TimeTable newTimeTable =
                      TimeTable.builder()
                          .name("기본 시간표")
                          .user(
                              userRepository
                                  .findByAccount(userDetails.getUsername())
                                  .orElseThrow(
                                      () ->
                                          new GeneralException(
                                              TimeTableErrorStatus.TIME_TABLE_NOT_FOUND)))
                          .build();
                  return timeTableRepository.save(newTimeTable);
                });
    List<Long> addedSubjectIds = new ArrayList<>();

    for (Long subjectId : subjectIds) {
      Subject subject =
          subjectRepository
              .findById(subjectId)
              .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.SUBJECT_NOT_FOUND));

      boolean isAlreadyAdded =
          timeTableSubjectRepository.existsByTimeTableAndSubject(timeTable, subject);
      if (!isAlreadyAdded) {
        TimeTableSubject timeTableSubject =
            TimeTableSubject.builder()
                .timeTable(timeTable)
                .subject(subject)
                .isCustomSubject(false)
                .build();

        timeTableSubjectRepository.save(timeTableSubject);
        addedSubjectIds.add(subject.getId());
      }
    }
    return new AddSubjectDTO(addedSubjectIds);
  }

  public TimeTableResponseDTO.SelfSubjectDTO addSelfSubject(
      UserDetails userDetails, selfSubjectDTO request) {
    Subject subject =
        subjectRepository.save(
            Subject.builder()
                .subject(request.getSubject())
                .day(request.getDay())
                .endTime(request.getEndTime())
                .startTime(request.getStartTime())
                .classroom(request.getClassroom())
                .credit("")
                .professor("")
                .grade("")
                .target("")
                .division(SubjectType.자유선택)
                .build());

    TimeTable timeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.TIME_TABLE_NOT_FOUND));

    boolean isAlreadyAdded =
        timeTableSubjectRepository.existsByTimeTableAndSubject(timeTable, subject);
    if (isAlreadyAdded) {
      throw new GeneralException(TimeTableErrorStatus.SUBJECT_ALREADY_EXIST);
    }

    TimeTableSubject timeTableSubject =
        TimeTableSubject.builder()
            .timeTable(timeTable)
            .subject(subject)
            .isCustomSubject(true)
            .build();

    timeTableSubjectRepository.save(timeTableSubject);
    return new TimeTableResponseDTO.SelfSubjectDTO(subject.getId());
  }

  public DeleteSubjectDTO deleteSubject(UserDetails userDetails, Long subjectId) {
    TimeTableSubject timeTableSubject =
        timeTableSubjectRepository
            .findByUserAccountBySubjectId(userDetails.getUsername(), subjectId)
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.SUBJECT_NOT_FOUND));
    timeTableSubjectRepository.deleteById(timeTableSubject.getId());
    return new DeleteSubjectDTO(subjectId);
  }

  public TimeTableResponseDTO.MyTimeTableDTO getSubjectsByDay(
      UserDetails userDetails, String dayOfWeek) {
    DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());

    TimeTable myTimeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.TIME_TABLE_NOT_FOUND));

    List<TimeTableSubject> mySubjects = timeTableSubjectRepository.findAllByTimeTable(myTimeTable);

    List<TimeTableResponseDTO.TimeTableDTO> subjects =
        mySubjects.stream()
            .map(TimeTableSubject::getSubject)
            .filter(subject -> isSubjectOnToday(subject, targetDay))
            .map(
                subject ->
                    new TimeTableResponseDTO.TimeTableDTO(
                        subject.getId(),
                        subject.getSubject(),
                        subject.getProfessor(),
                        subject.getDay(),
                        subject.getStartTime(),
                        subject.getStartTime(),
                        subject.getClassroom(),
                        subject.getCredit(),
                        subject.getGrade(),
                        subject.getTarget(),
                        subject.getDivision().name()))
            .collect(Collectors.toList());

    return new TimeTableResponseDTO.MyTimeTableDTO(subjects);
  }

  public TimeTableResponseDTO.TimeTableListDTO searchSubjects(String name) {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Subject> subjectPage = subjectRepository.findBySubjectName(name, pageable);
    List<TimeTableResponseDTO.TimeTableDTO> subjects =
        subjectPage.getContent().stream()
            .map(TimeTableResponseDTO.TimeTableDTO::new)
            .collect(Collectors.toList());

    boolean hasNext = subjectPage.hasNext();
    int nextPage = hasNext ? subjectPage.getNumber() + 1 : subjectPage.getNumber();
    return new TimeTableResponseDTO.TimeTableListDTO(subjects, hasNext, nextPage);
  }

  private boolean isSubjectOnToday(Subject subject, DayOfWeek today) {
    Map<String, DayOfWeek> dayMapping =
        Map.of(
            "월", DayOfWeek.MONDAY,
            "화", DayOfWeek.TUESDAY,
            "수", DayOfWeek.WEDNESDAY,
            "목", DayOfWeek.THURSDAY,
            "금", DayOfWeek.FRIDAY);

    return dayMapping.getOrDefault(subject.getDay(), null) == today;
  }

  @Scheduled(cron = "0 0 8 * * ?") // 매일 오전 8시에 실행
  @Transactional
  public void sendDailyTimeTablePush() {

    List<User> allUsers = userRepository.findAll();

    DayOfWeek today = LocalDate.now().getDayOfWeek();

    for (User user : allUsers) {
      TimeTable timeTable = timeTableRepository.findByUser_Account(user.getAccount()).orElse(null);

      if (timeTable == null) continue;

      List<TimeTableSubject> todaySubjects =
          timeTable.getTimeTableSubjects().stream()
              .filter(ts -> isSubjectOnToday(ts.getSubject(), today))
              .collect(Collectors.toList());

      if (todaySubjects.isEmpty()) continue;

      String pushTitle = "오늘의 수업";
      String pushBody =
          todaySubjects.stream()
              .map(
                  ts ->
                      ts.getSubject().getSubject()
                          + " ("
                          + ts.getSubject().getDay()
                          + ")"
                          + " ("
                          + ts.getSubject().getStartTime()
                          + ")"
                          + " ("
                          + ts.getSubject().getEndTime()
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
                .notificationType(NotificationType.NOTICE)
                .build();

        alarmRepository.save(alarm);
      }
    }
  }
}
