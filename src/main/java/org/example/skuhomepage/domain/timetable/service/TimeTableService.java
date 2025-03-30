package org.example.skuhomepage.domain.timetable.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO.selfSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.AddSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.DeleteSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MyTimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableListDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TodayTimeTableDTO;
import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.example.skuhomepage.domain.timetable.entity.SubjectType;
import org.example.skuhomepage.domain.timetable.entity.TimeTable;
import org.example.skuhomepage.domain.timetable.entity.mapping.TimeTableSubject;
import org.example.skuhomepage.domain.timetable.exception.TimeTableErrorStatus;
import org.example.skuhomepage.domain.timetable.repository.SubjectRepository;
import org.example.skuhomepage.domain.timetable.repository.TimeTableRepository;
import org.example.skuhomepage.domain.timetable.repository.TimeTableSubjectRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

  public TimeTableResponseDTO.TodayTimeTableListDTO getTodayTimeTable(UserDetails userDetails) {
    DayOfWeek today = LocalDate.now().getDayOfWeek();

    TimeTable timeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.TIME_TABLE_NOT_FOUND));

    List<TodayTimeTableDTO> todaySubjects =
        timeTable.getTimeTableSubjects().stream()
            .filter(ts -> isSubjectOnToday(ts.getSubject(), today))
            .map(
                ts ->
                    new TodayTimeTableDTO(
                        ts.getSubject().getId(),
                        ts.getSubject().getSubject(),
                        ts.getSubject().getTime(),
                        ts.getSubject().getClassroom(),
                        ts.getSubject().getTime()))
            .collect(Collectors.toList());
    return new TimeTableResponseDTO.TodayTimeTableListDTO(todaySubjects);
  }

  public MyTimeTableDTO getMyTimeTable(UserDetails userDetails) {
    TimeTable timeTable =
        timeTableRepository
            .findByUser_Account(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.TIMETABLE_NOT_FOUND));

    List<TimeTableResponseDTO.MySubjectDTO> subjects =
        timeTable.getTimeTableSubjects().stream()
            .map(
                ts ->
                    new TimeTableResponseDTO.MySubjectDTO(
                        ts.getSubject().getId(),
                        ts.getSubject().getSubject(),
                        ts.getSubject().getTime(),
                        ts.getSubject().getClassroom()))
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
                .time(request.getTime())
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

    List<TimeTableResponseDTO.MySubjectDTO> subjects =
        mySubjects.stream()
            .map(TimeTableSubject::getSubject)
            .filter(subject -> isSubjectOnToday(subject, targetDay))
            .map(
                subject ->
                    new TimeTableResponseDTO.MySubjectDTO(
                        subject.getId(),
                        subject.getSubject(),
                        subject.getTime(),
                        subject.getClassroom()))
            .collect(Collectors.toList());

    return new TimeTableResponseDTO.MyTimeTableDTO(subjects);
  }

  private boolean isSubjectOnToday(Subject subject, DayOfWeek today) {
    String time = subject.getTime();

    Map<String, DayOfWeek> dayMapping =
        Map.of(
            "월", DayOfWeek.MONDAY,
            "화", DayOfWeek.TUESDAY,
            "수", DayOfWeek.WEDNESDAY,
            "목", DayOfWeek.THURSDAY,
            "금", DayOfWeek.FRIDAY);

    String firstChar = time.substring(0, 1);
    return dayMapping.getOrDefault(firstChar, null) == today;
  }
}
