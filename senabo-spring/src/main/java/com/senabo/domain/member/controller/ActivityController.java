package com.senabo.domain.member.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.dto.request.CreateExpenseRequest;
import com.senabo.domain.member.dto.request.CreateWalkRequest;
import com.senabo.domain.member.dto.request.UpdateWalkRequest;
import com.senabo.domain.member.dto.response.*;
import com.senabo.domain.member.entity.*;
import com.senabo.domain.member.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/activity")
@Tag(name = "Activity", description = "Activity API Document")
public class ActivityController {
    private final ActivityService activityService;

    // 양치 create
    @PostMapping("/brushing")
    @Operation(summary = "양치 생성", description = "양치 시각을 저장한다.")
    public ApiResponse<BrushingTeethResponse> createBrushingTeeth(@RequestParam(name = "id") Long id){
        BrushingTeethResponse response = activityService.createBrushingTeeth(id);
        return ApiResponse.success("양치질 완료", response);
    }
    // 양치 read
    @GetMapping("/brushing")
    @Operation(summary = "양치 조회", description = "양치 내역을 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeeth(@RequestParam(name = "id") Long id){
        List<BrushingTeeth> response = activityService.getBrushingTeeth(id);
        List<BrushingTeethResponse> responseList = response.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치질 조회", responseList);
    }
    // 양치 delete
    @DeleteMapping("/brushing")
    @Operation(summary = "양치 삭제", description = "양치 내역을 삭제한다.")
    public ApiResponse<Object> removeBrushingTeeth(@RequestParam(name = "id") Long id) {
        activityService.removeBrushingTeeth(id);
        return ApiResponse.success("양치질 삭제", true);
    }

    // 배변 create
    @PostMapping("/poop")
    @Operation(summary = "배변 생성", description = "배변 생성 시각을 저장한다.")
    public ApiResponse<PoopResponse> createPoop(@RequestParam(name = "id") Long id){
        PoopResponse response = activityService.createPoop(id);
        return ApiResponse.success("배변 완료", response);
    }

    // 배변 read
    @GetMapping("/poop")
    @Operation(summary = "배변 조회", description = "배변 내역을 전체 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoop(@RequestParam(name = "id") Long id){
        List<Poop> response = activityService.getPoop(id);
        List<PoopResponse> responseList = response.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 조회", responseList);
    }
    // 배변 delete
    @DeleteMapping("/poop")
    @Operation(summary = "배변 내역 삭제", description = "배변 내역을 삭제한다.")
    public ApiResponse<Object> removePoop(@RequestParam(name = "id") Long id) {
        activityService.removePoop(id);
        return ApiResponse.success("배변 삭제", true);
    }
    // 배변 update
    @PatchMapping("/poop")
    @Operation(summary = "배변 청소 완료", description = "배변 청소 여부(cleanYn)를 true로 수정한다.")
    public ApiResponse<PoopResponse> updatePoop(@RequestParam(name = "id") Long id){
        PoopResponse response = activityService.updatePoop(id);
        return ApiResponse.success("배변 청소 완료", response);
    }

    // 교감 create
    @PostMapping("/communication/{type}")
    @Operation(summary = "교감 생성", description = "교감 생성 내용을 저장한다. WAIT(기다려), SIT(앉아), HAND(손), PAT(쓰다듬기), TUG(터그놀이) 5가지 타입이 있다.")
    public ApiResponse<CommunicationResponse> createCommunication(@RequestParam(name = "id") Long id, @RequestParam(name = "type") ActivityType type){
        CommunicationResponse response = activityService.createCommunication(id, type);
        return ApiResponse.success("교감 완료", response);
    }

    // 교감 read
    @GetMapping("/communication")
    @Operation(summary = "교감 조회", description = "교감 내역을 전체 조회한다.")
    public ApiResponse<List<CommunicationResponse>> getCommunication(@RequestParam(name = "id") Long id){
        List<Communication> response = activityService.getCommunication(id);
        List<CommunicationResponse> responseList = response.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("교감 조회", responseList);
    }

    // 교감 delete
    @DeleteMapping("/communication")
    @Operation(summary = "교감 내역 삭제", description = "교감 내역을 삭제한다.")
    public ApiResponse<Object> removeCommunication(@RequestParam(name = "id") Long id) {
        activityService.removeCommunication(id);
        return ApiResponse.success("교감 삭제", true);
    }

    // 산책 create-start
    @PostMapping("/walk")
    @Operation(summary = "산책 시작", description = "산책을 시작한다. 시작 시간(startTime)을 저장한다.")
    public ApiResponse<WalkResponse> createWalk(@RequestParam(name = "id") Long id, @RequestBody CreateWalkRequest request){
        WalkResponse response = activityService.createWalk(id, request);
        return ApiResponse.success("산책 시작", response);
    }
    // 산책 update-finish
    @PatchMapping("/walk")
    @Operation(summary = "산책 종료", description = "산책을 종료한다. 종료 시간(endTime)과 거리(distance)를 저장한다.")
    public ApiResponse<WalkResponse> updateWalk(@RequestParam(name = "id") Long id, @RequestBody UpdateWalkRequest request){
        WalkResponse response = activityService.updateWalk(id, request);
        return ApiResponse.success("산책 종료", response);
    }
    // 산책 read
    @GetMapping("/walk")
    @Operation(summary = "산책 조회", description = "산책 내역을 전체 조회한다.")
    public ApiResponse<List<WalkResponse>> getWalk(@RequestParam(name = "id") Long id){
        List<Walk> response = activityService.getWalk(id);
        List<WalkResponse> responseList = response.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("산책 조회", responseList);
    }
    // 산책 delete
    @DeleteMapping("/walk")
    @Operation(summary = "산책 내역 삭제", description = "산책 내역을 삭제한다.")
    public ApiResponse<Object> removeWalk(@RequestParam(name = "id") Long id) {
        activityService.removeWalk(id);
        return ApiResponse.success("산책 삭제", true);
    }

    // 비용 create
    @PostMapping("/expense")
    @Operation(summary = "비용 생성", description = "비용을 저장한다.")
    public ApiResponse<ExpenseResponse> createExpense(@RequestParam(name = "id") Long id, @RequestBody CreateExpenseRequest request){
        ExpenseResponse response = activityService.createExpense(id, request);
        return ApiResponse.success("비용 저장 완료", response);
    }

    // 비용 read
    @GetMapping("/expense")
    @Operation(summary = "비용 조회", description = "비용 내역을 전체 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpense(@RequestParam(name = "id") Long id){
        List<Expense> response = activityService.getExpense(id);
        List<ExpenseResponse> responseList = response.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 조회", responseList);
    }
    // 비용 delete
    @DeleteMapping("/expense")
    @Operation(summary = "비용 내역 삭제", description = "비용 내역을 삭제한다.")
    public ApiResponse<Object> removeExpense(@RequestParam(name = "id") Long id) {
        activityService.removeExpense(id);
        return ApiResponse.success("비용 삭제", true);
    }


    // 배식 create
    @PostMapping("/feed")
    @Operation(summary = "배식 생성", description = "배식을 저장한다.")
    public ApiResponse<FeedResponse> createFeed(@RequestParam(name = "id") Long id){
        FeedResponse response = activityService.createFeed(id);
        return ApiResponse.success("배식 저장 완료", response);
    }

    // 배식 read
    @GetMapping("/feed")
    @Operation(summary = "배식 조회", description = "배식 내역을 전체 조회한다.")
    public ApiResponse<List<FeedResponse>> getFeed(@RequestParam(name = "id") Long id){
        List<Feed> response = activityService.getFeed(id);
        List<FeedResponse> responseList = response.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배식 조회", responseList);
    }

    // 배식 delete
    @DeleteMapping("/feed")
    @Operation(summary = "배식 내역 삭제", description = "배식 내역을 삭제한다.")
    public ApiResponse<Object> removeFeed(@RequestParam(name = "id") Long id) {
        activityService.removeFeed(id);
        return ApiResponse.success("배식 삭제", true);
    }

    // 질병 create
    @PostMapping("/disease/{diseaseName}")
    @Operation(summary = "질병 생성", description = "질병을 저장한다.")
    public ApiResponse<DiseaseResponse> createDisease(@RequestParam(name = "id") Long id, @RequestParam(name = "diseaseName") String diseaseName){
        DiseaseResponse response = activityService.createDisease(id, diseaseName);
        return ApiResponse.success("질병 저장 완료", response);
    }

    // 질병 read
    @GetMapping("/disease")
    @Operation(summary = "질병 조회", description = "질병 내역을 전체 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDisease(@RequestParam(name = "id") Long id){
        List<Disease> response = activityService.getDisease(id);
        List<DiseaseResponse> responseList = response.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 조회", responseList);
    }

    // 질병 delete
    @DeleteMapping("/disease")
    @Operation(summary = "질병 내역 삭제", description = "질병 내역을 삭제한다.")
    public ApiResponse<Object> removeDisease(@RequestParam(name = "id") Long id) {
        activityService.removeDisease(id);
        return ApiResponse.success("질병 삭제", true);
    }

    // 목욕 create
    @PostMapping("/bath")
    @Operation(summary = "목욕 생성", description = "목욕을 저장한다.")
    public ApiResponse<BathResponse> createBath(@RequestParam(name = "id") Long id){
        BathResponse response = activityService.createBath(id);
        return ApiResponse.success("목욕 저장 완료", response);
    }

    // 목욕 read
    @GetMapping("/bath")
    @Operation(summary = "목욕 조회", description = "목욕 내역을 전체 조회한다.")
    public ApiResponse<List<BathResponse>> getBath(@RequestParam(name = "id") Long id){
        List<Bath> response = activityService.getBath(id);
        List<BathResponse> responseList = response.stream()
                .map(BathResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("목욕 조회", responseList);
    }

    // 목욕 delete
    @DeleteMapping("/bath")
    @Operation(summary = "목욕 내역 삭제", description = "목욕 내역을 삭제한다.")
    public ApiResponse<Object> removeBath(@RequestParam(name = "id") Long id) {
        activityService.removeBath(id);
        return ApiResponse.success("목욕 삭제", true);
    }
}
