package com.senabo.domain.feed.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.feed.dto.response.CheckFeedResponse;
import com.senabo.domain.feed.dto.response.FeedResponse;
import com.senabo.domain.feed.entity.Feed;
import com.senabo.domain.feed.service.FeedService;
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
@RequestMapping("/api/feed")
@Tag(name = "Feed", description = "Feed API Document")
public class FeedContainer {

    private final FeedService feedService;

    @PostMapping("/save")
    @Operation(summary = "배식 저장", description = "배식을 저장한다.")
    public ApiResponse<FeedResponse> createFeed(@RequestParam String email) {
        FeedResponse response = feedService.createFeed(email);
        return ApiResponse.success("배식 저장 성공", response);
    }


    @GetMapping("/list")
    @Operation(summary = "배식 전체 조회", description = "배식 내역을 전체 조회한다.")
    public ApiResponse<Map<String, Object>> getFeed(@RequestParam String email) {
        List<Feed> feed = feedService.getFeed(email);
        if (feed.isEmpty()) return ApiResponse.fail("배식 전체 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("feedList", feed.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("배식 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @Operation(summary = "배식 주간 조회", description = "배식 내역을 주간 조회한다.")
    public ApiResponse<Map<String, Object>> getFeedWeek(@RequestParam String email, @PathVariable int week) {
        List<Feed> feed = feedService.getFeedWeek(email, week);
        if (feed.isEmpty()) return ApiResponse.fail("배식 " + week + "주차 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("feedList", feed.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("배식 " + week + "주차 조회 성공", response);
    }


    @GetMapping("/check")
    @Operation(summary = "배식 가능 여부 확인", description = "마지막 배식시간이 현재 시각으로 부터 12시간이 경과했는지 확인한다. 가능하면 ture, 불가능하면 false를 return한다.")
    public ApiResponse<Map<String, Object>> checkLastFeed(@RequestParam String email) {
        CheckFeedResponse checkLastFeed = feedService.checkLastFeed(email);
        Map<String, Object> response = new HashMap<>();
        response.put("feedCheckList", checkLastFeed);
        return ApiResponse.success("배식 가능 여부 확인 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "배식 내역 삭제", description = "배식 내역을 삭제한다.")
    public ApiResponse<Object> removeFeed(@RequestParam String email) {
        feedService.removeFeed(email);
        return ApiResponse.success("배식 삭제 성공", true);
    }
}
