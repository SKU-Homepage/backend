package org.example.skuhomepage.domain.timetable.service;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface TimeTableService {
  TimeTableResponseDTO.TodayTimeTableDTO getTodayTimeTable(UserDetails userDetails);

  TimeTableResponseDTO.MyTimeTableDTO getMyTimeTable(UserDetails userDetails);

  TimeTableResponseDTO.TimeTableListDTO getTimeTableList(UserDetails userDetails);

  TimeTableResponseDTO.AddSubjectDTO addSubject(UserDetails userDetails, Long subjectId);

  TimeTableResponseDTO.AddSubjectDTO addSelfSubject(
      UserDetails userDetails, TimeTableRequestDTO.selfSubjectDTO request);

  TimeTableResponseDTO.DeleteSubjectDTO deleteSubject(UserDetails userDetails, Long subjectId);
}
