package org.example.skuhomepage.domain.timetable.service;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO.selfSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.AddSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.DeleteSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MyTimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableListDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TodayTimeTableDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeTableService {

  public TodayTimeTableDTO getTodayTimeTable(UserDetails userDetails) {
    return null;
  }

  public MyTimeTableDTO getMyTimeTable(UserDetails userDetails) {
    return null;
  }

  public TimeTableListDTO getTimeTableList(UserDetails userDetails) {
    return null;
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
