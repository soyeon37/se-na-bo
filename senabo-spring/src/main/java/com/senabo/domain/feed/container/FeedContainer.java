package com.senabo.domain.feed.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.feed.dto.response.FeedResponse;
import com.senabo.domain.feed.entity.Feed;
import com.senabo.domain.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/feed")
    @Operation(summary = "배식 생성", description = "배식을 저장한다.")
    public ApiResponse<FeedResponse> createFeed(@RequestParam(name = "id") Long id) {
        FeedResponse response = feedService.createFeed(id);
        return ApiResponse.success("배식 저장 완료", response);
    }


    @GetMapping("/feed")
    @Operation(summary = "배식 전체 조회", description = "배식 내역을 전체 조회한다.")
    public ApiResponse<List<FeedResponse>> getFeed(@RequestParam(name = "id") Long id) {
        List<Feed> response = feedService.getFeed(id);
        List<FeedResponse> responseList = response.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배식 전체 조회", responseList);
    }


    @GetMapping("/feed/{week}")
    @Operation(summary = "배식 주간 조회", description = "배식 내역을 주간 조회한다.")
    public ApiResponse<List<FeedResponse>> getFeedWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<Feed> response = feedService.getFeedWeek(id, week);
        List<FeedResponse> responseList = response.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배식 주간 조회", responseList);
    }


    @GetMapping("/feed/check")
    @Operation(summary = "배식 가능 여부 확인", description = "마지막 배식시간이 현재 시각으로 부터 12시간이 경과했는지 확인한다. 가능하면 ture, 불가능하면 false를 return한다.")
    public ApiResponse<Object> checkFeed(@RequestParam(name = "id") Long id) {
        Map<String, Object> response = feedService.checkLastFeed(id);
        return ApiResponse.success("배식 가능 여부 확인", response);
    }


    @DeleteMapping("/feed")
    @Operation(summary = "배식 내역 삭제", description = "배식 내역을 삭제한다.")
    public ApiResponse<Object> removeFeed(@RequestParam(name = "id") Long id) {
        feedService.removeFeed(id);
        return ApiResponse.success("배식 삭제", true);
    }
}
