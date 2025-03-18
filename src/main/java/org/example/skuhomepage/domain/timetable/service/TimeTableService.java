package org.example.skuhomepage.domain.timetable.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO.selfSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.AddSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.DeleteSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MyTimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableListDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TodayTimeTableDTO;
import org.example.skuhomepage.domain.timetable.entity.Subject;
import org.example.skuhomepage.domain.timetable.repository.SubjectRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeTableService {
  private final SubjectRepository subjectRepository;

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
    return null;
  }

  public AddSubjectDTO addSelfSubject(UserDetails userDetails, selfSubjectDTO request) {
    return null;
  }

  public DeleteSubjectDTO deleteSubject(UserDetails userDetails, Long subjectId) {
    return null;
  }
}
