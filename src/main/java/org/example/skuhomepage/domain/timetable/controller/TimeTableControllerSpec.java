package org.example.skuhomepage.domain.timetable.controller;

import jakarta.validation.Valid;

import org.example.skuhomepage.domain.timetable.dto.TimeTableRequestDTO;
import org.example.skuhomepage.domain.timetable.dto.TimeTableResponseDTO;
import org.example.skuhomepage.domain.timetable.exception.TimeTableErrorStatus;
import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "timetable-controller", description = "시간표 관련 API")
@RequestMapping("/api/time-table")
public interface TimeTableControllerSpec {

  @GetMapping("/today")
  @Operation(summary = "오늘의 수업시간표 조회하기", description = "오늘의 수업시간표를 조회하는 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.TodayTimeTableListDTO> getTodayTimeTable(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @GetMapping("/myTimeTable")
  @Operation(summary = "나의 수업시간표 조회하기", description = "나의 수업시간표를 조회하는 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> getMyTimeTable(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails);

  @GetMapping("/")
  @Operation(summary = "수업시간표 전체 목록 조회하기", description = "수업시간표 목록 전체 조회하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.TimeTableListDTO> getTimeTableList(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size);

  @PostMapping("/add")
  @Operation(summary = "선택한 과목 내 시간표에 추가하기", description = "선택한 수업 내 시간표에 추가하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.AddSubjectDTO> addSubject(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "subjectId", description = "과목 아이디", required = true) @RequestBody
          TimeTableRequestDTO.AddSubjectDTO subjectDTO);

  @PostMapping("/self")
  @Operation(summary = "수업 직접 추가하기", description = "수업 직접 추가하는 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.SelfSubjectDTO> addSelfSubject(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Valid @RequestBody TimeTableRequestDTO.selfSubjectDTO request);

  @DeleteMapping("/{subjectId}")
  @Operation(summary = "선택한 과목 내 시간표에서 삭제하기", description = "선택한 수업 내 시간표에서 삭제하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.DeleteSubjectDTO> deleteSubject(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "subjectId", description = "과목 아이디", required = true) @PathVariable
          Long subjectId);

  @GetMapping("/weekly/{dayOfWeek}")
  @Operation(summary = "요일별 시간표 조회하기", description = "요일별 시간표 조회하기")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.MyTimeTableDTO> dailyTimeTable(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(description = "조회할 요일", example = "MONDAY") @PathVariable String dayOfWeek);

  @GetMapping("/search/name")
  @Operation(summary = "과목명으로 과목 검색하기", description = "과목 검색하기 api")
  @ApiErrorCodeExample(TimeTableErrorStatus.class)
  ApiResponse<TimeTableResponseDTO.TimeTableListDTO> searchTimeTable(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
      @Parameter(description = "검색할 과목명", example = "알고리즘") @RequestParam String name);
}
