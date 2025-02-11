package org.example.skuhomepage.domain.timetable.controller;

import java.util.List;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.AddSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.DeleteSubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MySubjectDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.MyTimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TimeTableListDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO.TodayTimeTableDTO;
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
    return ApiResponse.onSuccess(new TodayTimeTableDTO("1교시", "자료구조", "북악관 608호"));
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> getMyTimeTable(UserDetails userDetails) {
    TimeTableResponseDTO.MyTimeTableDTO result = timeTableService.getMyTimeTable(userDetails);
    return ApiResponse.onSuccess(
        new MyTimeTableDTO(
            List.of(
                new MySubjectDTO(1L, "자료구조", "월 1교시", "북악관 608호"),
                new MySubjectDTO(2L, "알고리즘", "화 1교시", "북악관 607호"),
                new MySubjectDTO(3L, "네트워크", "수 1교시", "북악관 610호"))));
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.TimeTableListDTO> getTimeTableList(
      UserDetails userDetails) {
    TimeTableResponseDTO.TimeTableListDTO result = timeTableService.getTimeTableList(userDetails);
    return ApiResponse.onSuccess(
        new TimeTableListDTO(
            List.of(
                new TimeTableDTO(1L, "자료구조", "이지영", "월 1교시", "북악관 608호", 3, 1, "컴퓨터공학과", "전공"),
                new TimeTableDTO(2L, "알고리즘", "이지영", "화 1교시", "북악관 607호", 3, 1, "컴퓨터공학과", "전공"),
                new TimeTableDTO(3L, "네트워크", "이지영", "수 1교시", "북악관 610호", 3, 1, "컴퓨터공학과", "전공"))));
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSubject(
      UserDetails userDetails, Long subjectId) {
    TimeTableResponseDTO.AddSubjectDTO result = timeTableService.addSubject(userDetails, subjectId);
    return ApiResponse.onSuccess(new AddSubjectDTO(1L));
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSelfSubject(
      UserDetails userDetails, TimeTableRequestDTO.selfSubjectDTO request) {
    TimeTableResponseDTO.AddSubjectDTO result =
        timeTableService.addSelfSubject(userDetails, request);
    return ApiResponse.onSuccess(new AddSubjectDTO(1L));
  }

  @Override
  public ApiResponse<TimeTableResponseDTO.DeleteSubjectDTO> deleteSubject(
      UserDetails userDetails, Long subjectId) {
    TimeTableResponseDTO.DeleteSubjectDTO result =
        timeTableService.deleteSubject(userDetails, subjectId);
    return ApiResponse.onSuccess(new DeleteSubjectDTO(1L));
  }
}
