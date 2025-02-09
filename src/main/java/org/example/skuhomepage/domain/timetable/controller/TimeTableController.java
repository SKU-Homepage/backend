package org.example.skuhomepage.domain.timetable.controller;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.service.TimeTableService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TimeTableController implements TimeTableControllerSpec {
  private final TimeTableService timeTableService;

  @Override
  public ApiResponse<TimeTableResponseDTO.TodayTimeTableDTO> getTodayTimeTable(
      UserDetails userDetails) {
    TimeTableResponseDTO.TodayTimeTableDTO result = timeTableService.getTodayTimeTable(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> getMyTimeTable(UserDetails userDetails) {
    TimeTableResponseDTO.MyTimeTableDTO result = timeTableService.getMyTimeTable(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.TimeTableListDTO> getTimeTableList(
      UserDetails userDetails) {
    TimeTableResponseDTO.TimeTableListDTO result = timeTableService.getTimeTableList(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSubject(
      UserDetails userDetails, Long subjectId) {
    TimeTableResponseDTO.AddSubjectDTO result = timeTableService.addSubject(userDetails, subjectId);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSelfSubject(
      UserDetails userDetails, TimeTableRequestDTO.selfSubjectDTO request) {
    TimeTableResponseDTO.AddSubjectDTO result =
        timeTableService.addSelfSubject(userDetails, request);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.DeleteSubjectDTO> deleteSubject(
      UserDetails userDetails, Long subjectId) {
    TimeTableResponseDTO.DeleteSubjectDTO result =
        timeTableService.deleteSubject(userDetails, subjectId);
    return ApiResponse.onSuccess(result);
  }
}
