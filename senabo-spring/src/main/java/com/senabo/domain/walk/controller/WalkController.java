package com.senabo.domain.walk.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.stress.dto.response.StressResponse;
import com.senabo.domain.walk.dto.request.UpdateWalkRequest;
import com.senabo.domain.walk.dto.response.TodayWalkResponse;
import com.senabo.domain.walk.dto.response.WalkResponse;
import com.senabo.domain.walk.entity.Walk;
import com.senabo.domain.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = WalkResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "산책 전체 조회", description = "산책 내역을 전체 조회한다.")
    public ApiResponse<List<WalkResponse>> getWalk(@RequestParam String email) {
        List<Walk> walk = walkService.getWalk(email);
        if (walk.isEmpty()) return ApiResponse.fail("산책 전체 조회 성공", null);
        List<WalkResponse> response = walk.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("산책 전체 조회 성공", response);
    }

    @GetMapping("/list/{week}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = WalkResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "산책 주간 조회", description = "산책 내역을 주간 조회한다.")
    public ApiResponse<List<WalkResponse>>  getWalkWeek(@RequestParam String email, @PathVariable int week) {
        List<Walk> walk = walkService.getWalkWeek(email, week);
        if (walk.isEmpty()) return ApiResponse.fail("산책 " + week + "주차 조회 실패", null);
        List<WalkResponse> response = walk.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("산책 " + week + "주차 조회 성공", response);
    }

    @GetMapping("/today")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = WalkResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "산책 당일 조회", description = "산책 내역을 당일 조회한다.")
    public ApiResponse<TodayWalkResponse>  getTodayWalk(@RequestParam String email) {
        List<Walk> walk = walkService.getTodayWalk(email);
        if (walk.isEmpty()) return ApiResponse.fail("산책 당일 조회 실패", null);
        TodayWalkResponse todayTotalList = walkService.getTodayTotal(walk);
        return ApiResponse.success("산책 당일 조회 성공", todayTotalList);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "산책 내역 삭제", description = "산책 내역을 삭제한다.")
    public ApiResponse<Object> removeWalk(@RequestParam String email) {
        walkService.removeWalk(email);
        return ApiResponse.success("산책 삭제 성공", true);
    }
}
