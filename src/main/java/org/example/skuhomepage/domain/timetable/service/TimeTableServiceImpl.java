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
public class TimeTableServiceImpl implements TimeTableService {

  @Override
  public TodayTimeTableDTO getTodayTimeTable(UserDetails userDetails) {
    return null;
  }

  @Override
  public MyTimeTableDTO getMyTimeTable(UserDetails userDetails) {
    return null;
  }

  @Override
  public TimeTableListDTO getTimeTableList(UserDetails userDetails) {
    return null;
  }

  @Override
  public AddSubjectDTO addSubject(UserDetails userDetails, Long subjectId) {
    return null;
  }

  @Override
  public AddSubjectDTO addSelfSubject(UserDetails userDetails, selfSubjectDTO request) {
    return null;
  }

  @Override
  public DeleteSubjectDTO deleteSubject(UserDetails userDetails, Long subjectId) {
    return null;
  }
}
