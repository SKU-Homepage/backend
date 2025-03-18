package org.example.skuhomepage.domain.timetable.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO.selfSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.AddSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.DeleteSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MyTimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableListDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TodayTimeTableDTO;
import org.example.skuhomepage.domain.timetable.entity.Subject;
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

  public TodayTimeTableDTO getTodayTimeTable(UserDetails userDetails) {
    return null;
  }

  public MyTimeTableDTO getMyTimeTable(UserDetails userDetails) {
    return null;
  }

  public TimeTableListDTO getTimeTableList(UserDetails userDetails, Pageable pageable) {
    Slice<Subject> subjectList = subjectRepository.findAll(pageable);

    List<TimeTableResponseDTO.TimeTableDTO> timeTableDTOS =
        subjectList.getContent().stream()
            .map(TimeTableResponseDTO.TimeTableDTO::new)
            .collect(Collectors.toList());

    return new TimeTableListDTO(timeTableDTOS, subjectList.hasNext(), pageable.getPageNumber() + 1);
  }

  public AddSubjectDTO addSubject(UserDetails userDetails, Long subjectId) {
    Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new GeneralException(TimeTableErrorStatus.SUBJECT_NOT_FOUND));
    TimeTable timeTable = timeTableRepository.findByUser_Account(userDetails.getUsername())
            .orElseThrow(()-> new GeneralException(TimeTableErrorStatus.TIME_TABLE_NOT_FOUND));

    boolean isAlreadyAdded = timeTableSubjectRepository.existsByTimeTableAndSubject(timeTable, subject);
    if (isAlreadyAdded) {
      throw new GeneralException(TimeTableErrorStatus.SUBJECT_ALREADY_EXIST);
    }

    TimeTableSubject timeTableSubject = TimeTableSubject.builder()
            .timeTable(timeTable)
            .subject(subject)
            .isCustomSubject(false)
            .build();

    timeTableSubjectRepository.save(timeTableSubject);
    return new AddSubjectDTO(subject.getId());
  }

  public AddSubjectDTO addSelfSubject(UserDetails userDetails, selfSubjectDTO request) {
    return null;
  }

  public DeleteSubjectDTO deleteSubject(UserDetails userDetails, Long subjectId) {
    return null;
  }
}
