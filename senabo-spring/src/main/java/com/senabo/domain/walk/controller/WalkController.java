package com.senabo.domain.walk.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.walk.dto.request.UpdateWalkRequest;
import com.senabo.domain.walk.dto.response.WalkResponse;
import com.senabo.domain.walk.entity.Walk;
import com.senabo.domain.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/walk")
@Tag(name = "Walk", description = "Walk API Document")
public class WalkController {
    private final WalkService walkService;
    private final MemberService memberService;

    @PostMapping("/start")
    @Operation(summary = "산책 시작", description = "산책을 시작한다. 시작 시간(startTime)을 저장한다.")
    public ApiResponse<WalkResponse> createWalk(@RequestParam String email) {
        WalkResponse response = walkService.createWalk(email);
        return ApiResponse.success("산책 시작 성공", response);
    }


    @PatchMapping("/end")
    @Operation(summary = "산책 종료", description = "산책을 종료한다. 종료 시간(endTime)과 거리(distance)를 저장한다.")
    public ApiResponse<WalkResponse> updateWalk(@RequestParam String email, @RequestBody UpdateWalkRequest request) {
        WalkResponse response = walkService.updateWalk(email, request);
        return ApiResponse.success("산책 종료 성공", response);
    }


    @GetMapping("/list")
    @Operation(summary = "산책 전체 조회", description = "산책 내역을 전체 조회한다.")
    public ApiResponse<Map<String ,Object>> getWalk(@RequestParam String email) {
        List<Walk> walk = walkService.getWalk(email);
        if (walk.isEmpty()) return ApiResponse.fail("산책 전체 조회 성공", null);
        Map<String, Object> response = new HashMap<>();
        response.put("walkList", walk.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("산책 전체 조회 성공", response);
    }

    @GetMapping("/list/{week}")
    @Operation(summary = "산책 주간 조회", description = "산책 내역을 주간 조회한다.")
    public ApiResponse<Map<String ,Object>>  getWalkWeek(@RequestParam String email, @PathVariable int week) {
        List<Walk> walk = walkService.getWalkWeek(email, week);
        if (walk.isEmpty()) return ApiResponse.fail("산책 " + week + "주차 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("walkList", walk.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("산책 " + week + "주차 조회 성공", response);
    }

    @GetMapping("/today")
    @Operation(summary = "산책 당일 조회", description = "산책 내역을 당일 조회한다.")
    public ApiResponse<Map<String ,Object>>  getTodayWalk(@RequestParam String email) {
        List<Walk> walk = walkService.getTodayWalk(email);
        if (walk.isEmpty()) return ApiResponse.fail("산책 당일 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("walkList", walk.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList()));
        Map<String, Object> todayTotalList = walkService.getTodayTotal(walk);
        response.put("todayTotalWalkTime", todayTotalList.get("todayTotalWalkTime"));
        response.put("todayTotalWalkDistance", todayTotalList.get("todayTotalWalkDistance"));
        return ApiResponse.success("산책 당일 조회 성공", response);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "산책 내역 삭제", description = "산책 내역을 삭제한다.")
    public ApiResponse<Object> removeWalk(@RequestParam String email) {
        walkService.removeWalk(email);
        return ApiResponse.success("산책 삭제 성공", true);
    }
}
