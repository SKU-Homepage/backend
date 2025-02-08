package org.example.skuhomepage.domain.timetable.controller;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.exception.TimeTableErrorStatus;
import org.example.skuhomepage.domain.timetable.service.TimeTableService;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "timetable-controller", description = "시간표 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/time-table")
public class TimeTableController {
  private final TimeTableService timeTableService;

  @GetMapping("/today")
  @Operation(summary = "오늘의 수업시간표 조회하기", description = "오늘의 수업시간표를 조회하는 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  public ApiResponse<TimeTableResponseDTO.TodayTimeTableDTO> getTodayTimeTable(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    TimeTableResponseDTO.TodayTimeTableDTO result = timeTableService.getTodayTimeTable(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @GetMapping("/myTimeTable")
  @Operation(summary = "나의 수업시간표 조회하기", description = "나의 수업시간표를 조회하는 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  public ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> getMyTimeTable(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    TimeTableResponseDTO.MyTimeTableDTO result = timeTableService.getMyTimeTable(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @GetMapping("/")
  @Operation(summary = "수업시간표 전체 목록 조회하기", description = "수업시간표 목록 전체 조회하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  public ApiResponse<TimeTableResponseDTO.TimeTableListDTO> getTimeTableList(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    TimeTableResponseDTO.TimeTableListDTO result = timeTableService.getTimeTableList(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @PostMapping("/{subjectId}")
  @Operation(summary = "선택한 과목 내 시간표에 추가하기", description = "선택한 수업 내 시간표에 추가하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSubject(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "subjectId", description = "과목 아이디", required = true) @PathVariable
          Long subjectId) {
    TimeTableResponseDTO.AddSubjectDTO result = timeTableService.addSubject(userDetails, subjectId);
    return ApiResponse.onSuccess(result);
  }

  @PostMapping("/self")
  @Operation(summary = "수업 직접 추가하기", description = "수업 직접 추가하는 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  public ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSelfSubject(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @RequestBody(description = "사용자가 직접 입력한 수업 정보", required = true)
          TimeTableRequestDTO.selfSubjectDTO request) {
    TimeTableResponseDTO.AddSubjectDTO result =
        timeTableService.addSelfSubject(userDetails, request);
    return ApiResponse.onSuccess(result);
  }

  @DeleteMapping("/{subjectId}")
  @Operation(summary = "선택한 과목 내 시간표에서 삭제하기", description = "선택한 수업 내 시간표에서 삭제하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  public ApiResponse<TimeTableResponseDTO.DeleteSubjectDTO> deleteSubject(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "subjectId", description = "과목 아이디", required = true) @PathVariable
          Long subjectId) {
    TimeTableResponseDTO.DeleteSubjectDTO result =
        timeTableService.deleteSubject(userDetails, subjectId);
    return ApiResponse.onSuccess(result);
  }
}
