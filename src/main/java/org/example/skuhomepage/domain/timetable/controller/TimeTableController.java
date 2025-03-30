package org.example.skuhomepage.domain.timetable.controller;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.service.TimeTableService;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TimeTableController implements TimeTableControllerSpec {
  private final TimeTableService timeTableService;

  @Override
  public ApiResponse<TimeTableResponseDTO.TodayTimeTableListDTO> getTodayTimeTable(
      UserDetails userDetails) {
    TimeTableResponseDTO.TodayTimeTableListDTO result =
        timeTableService.getTodayTimeTable(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> getMyTimeTable(UserDetails userDetails) {
    TimeTableResponseDTO.MyTimeTableDTO result = timeTableService.getMyTimeTable(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.TimeTableListDTO> getTimeTableList(
      UserDetails userDetails, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    TimeTableResponseDTO.TimeTableListDTO result =
        timeTableService.getTimeTableList(userDetails, pageable);
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSubject(
      UserDetails userDetails, TimeTableRequestDTO.AddSubjectDTO subjectDTO) {
    TimeTableResponseDTO.AddSubjectDTO result =
        timeTableService.addSubject(userDetails, subjectDTO.getSubjectIds());
    return ApiResponse.onSuccess(result);
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.SelfSubjectDTO> addSelfSubject(
      UserDetails userDetails, TimeTableRequestDTO.selfSubjectDTO request) {
    TimeTableResponseDTO.SelfSubjectDTO result =
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

  @Override
  public ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> dailyTimeTable(
      UserDetails userDetails, String dayOfWeek) {
    TimeTableResponseDTO.MyTimeTableDTO result =
        timeTableService.getSubjectsByDay(userDetails, dayOfWeek);
    return ApiResponse.onSuccess(result);
  }
}
