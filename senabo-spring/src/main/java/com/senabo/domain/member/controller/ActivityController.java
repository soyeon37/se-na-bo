package com.senabo.domain.member.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.dto.request.CreateExpenseRequest;
import com.senabo.domain.member.dto.request.CreateWalkRequest;
import com.senabo.domain.member.dto.request.UpdateTotalTimeRequest;
import com.senabo.domain.member.dto.request.UpdateWalkRequest;
import com.senabo.domain.member.dto.response.*;
import com.senabo.domain.member.entity.*;
import com.senabo.domain.member.service.ActivityService;
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
@RequestMapping("/api/activity")
@Tag(name = "Activity", description = "Activity API Document")
public class ActivityController {
    private final ActivityService activityService;


    @PostMapping("/brushing")
    @Operation(summary = "양치 내역 생성", description = "양치 시각을 저장한다.")
    public ApiResponse<BrushingTeethResponse> createBrushingTeeth(@RequestParam(name = "id") Long id){
        BrushingTeethResponse response = activityService.createBrushingTeeth(id);
        return ApiResponse.success("양치질 완료", response);
    }


    @GetMapping("/brushing")
    @Operation(summary = "양치 내역 전체 조회", description = "양치 내역을 전체 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeeth(@RequestParam(name = "id") Long id){
        List<BrushingTeeth> response = activityService.getBrushingTeeth(id);
        List<BrushingTeethResponse> responseList = response.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치질 전체 조회", responseList);
    }


    @GetMapping("/brushing/{week}")
    @Operation(summary = "양치 주간 내역 조회", description = "양치 내역을 주간 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeethWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<BrushingTeeth> response = activityService.getBrushingTeethWeek(id, week);
        List<BrushingTeethResponse> responseList = response.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치질 주간 조회", responseList);
    }


    @DeleteMapping("/brushing")
    @Operation(summary = "양치 삭제", description = "양치 내역을 삭제한다.")
    public ApiResponse<Object> removeBrushingTeeth(@RequestParam(name = "id") Long id) {
        activityService.removeBrushingTeeth(id);
        return ApiResponse.success("양치질 삭제", true);
    }


    @PostMapping("/poop")
    @Operation(summary = "배변 생성", description = "배변 생성 시각을 저장한다.")
    public ApiResponse<PoopResponse> createPoop(@RequestParam(name = "id") Long id){
        PoopResponse response = activityService.createPoop(id);
        return ApiResponse.success("배변 완료", response);
    }


    @GetMapping("/poop")
    @Operation(summary = "배변 전체 조회", description = "배변 내역을 전체 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoop(@RequestParam(name = "id") Long id){
        List<Poop> response = activityService.getPoop(id);
        List<PoopResponse> responseList = response.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 전체 조회", responseList);
    }


    @GetMapping("/poop/{week}")
    @Operation(summary = "배변 주간 조회", description = "배변 내역을 주간 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoopWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Poop> response = activityService.getPoopWeek(id, week);
        List<PoopResponse> responseList = response.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 주간 조회", responseList);
    }


    @DeleteMapping("/poop")
    @Operation(summary = "배변 내역 삭제", description = "배변 내역을 삭제한다.")
    public ApiResponse<Object> removePoop(@RequestParam(name = "id") Long id) {
        activityService.removePoop(id);
        return ApiResponse.success("배변 삭제", true);
    }


    @PatchMapping("/poop")
    @Operation(summary = "배변 청소 완료", description = "배변 청소 여부(cleanYn)를 true로 수정한다.")
    public ApiResponse<PoopResponse> updatePoop(@RequestParam(name = "id") Long id){
        PoopResponse response = activityService.updatePoop(id);
        return ApiResponse.success("배변 청소 완료", response);
    }


    @PostMapping("/communication/{type}")
    @Operation(summary = "교감 생성", description = "교감 생성 내용을 저장한다. WAIT(기다려), SIT(앉아), HAND(손), PAT(쓰다듬기), TUG(터그놀이) 5가지 타입이 있다.")
    public ApiResponse<CommunicationResponse> createCommunication(@RequestParam(name = "id") Long id, @RequestParam(name = "type") ActivityType type){
        CommunicationResponse response = activityService.createCommunication(id, type);
        return ApiResponse.success("교감 완료", response);
    }


    @GetMapping("/communication")
    @Operation(summary = "교감 전체 조회", description = "교감 내역을 전체 조회한다.")
    public ApiResponse<List<CommunicationResponse>> getCommunication(@RequestParam(name = "id") Long id){
        List<Communication> response = activityService.getCommunication(id);
        List<CommunicationResponse> responseList = response.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("교감 전체 조회", responseList);
    }

    @GetMapping("/communication/{week}")
    @Operation(summary = "교감 주간 조회", description = "교감 내역을 주간 조회한다.")
    public ApiResponse<List<CommunicationResponse>> getCommunication(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Communication> response = activityService.getCommunicationWeek(id, week);
        List<CommunicationResponse> responseList = response.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("교감 주간 조회", responseList);
    }



    @DeleteMapping("/communication")
    @Operation(summary = "교감 내역 삭제", description = "교감 내역을 삭제한다.")
    public ApiResponse<Object> removeCommunication(@RequestParam(name = "id") Long id) {
        activityService.removeCommunication(id);
        return ApiResponse.success("교감 삭제", true);
    }


    @PostMapping("/walk")
    @Operation(summary = "산책 시작", description = "산책을 시작한다. 시작 시간(startTime)을 저장한다.")
    public ApiResponse<WalkResponse> createWalk(@RequestParam(name = "id") Long id, @RequestBody CreateWalkRequest request){
        WalkResponse response = activityService.createWalk(id, request);
        return ApiResponse.success("산책 시작", response);
    }


    @PatchMapping("/walk")
    @Operation(summary = "산책 종료", description = "산책을 종료한다. 종료 시간(endTime)과 거리(distance)를 저장한다.")
    public ApiResponse<WalkResponse> updateWalk(@RequestParam(name = "id") Long id, @RequestBody UpdateWalkRequest request){
        WalkResponse response = activityService.updateWalk(id, request);
        return ApiResponse.success("산책 종료", response);
    }


    @GetMapping("/walk")
    @Operation(summary = "산책 전체 조회", description = "산책 내역을 전체 조회한다.")
    public ApiResponse<List<WalkResponse>> getWalk(@RequestParam(name = "id") Long id){
        List<Walk> response = activityService.getWalk(id);
        List<WalkResponse> responseList = response.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("산책 전체 조회", responseList);
    }

    @GetMapping("/walk/{week}")
    @Operation(summary = "산책 주간 조회", description = "산책 내역을 주간 조회한다.")
    public ApiResponse<List<WalkResponse>> getWalkWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Walk> response = activityService.getWalkWeek(id, week);
        List<WalkResponse> responseList = response.stream()
                .map(WalkResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("산책 주간 조회", responseList);
    }

    @DeleteMapping("/walk")
    @Operation(summary = "산책 내역 삭제", description = "산책 내역을 삭제한다.")
    public ApiResponse<Object> removeWalk(@RequestParam(name = "id") Long id) {
        activityService.removeWalk(id);
        return ApiResponse.success("산책 삭제", true);
    }


    @PostMapping("/expense")
    @Operation(summary = "비용 생성", description = "비용을 저장한다.")
    public ApiResponse<ExpenseResponse> createExpense(@RequestParam(name = "id") Long id, @RequestBody CreateExpenseRequest request){
        ExpenseResponse response = activityService.createExpense(id, request);
        return ApiResponse.success("비용 저장 완료", response);
    }


    @GetMapping("/expense")
    @Operation(summary = "비용 전체 조회", description = "비용 내역을 전체 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpense(@RequestParam(name = "id") Long id){
        List<Expense> response = activityService.getExpense(id);
        List<ExpenseResponse> responseList = response.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 전체 조회", responseList);
    }


    @GetMapping("/expense/{week}")
    @Operation(summary = "비용 주간 조회", description = "비용 내역을 주간 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpense(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Expense> response = activityService.getExpenseWeek(id, week);
        List<ExpenseResponse> responseList = response.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 주간 조회", responseList);
    }


    @GetMapping("/expense/total")
    @Operation(summary = "비용 총 금액 조회", description = "비용 총 금액을 조회한다.")
    public ApiResponse<Map<String, Object>> getTotalExpense(@RequestParam(name = "id") Long id){
        Map<String, Object> map = new HashMap<>();
        Long totalAmount = activityService.getExpenseTotal(id);
        map.put("totalAmount", totalAmount);
        return ApiResponse.success("비용 총 금액 조회", map);
    }


    @GetMapping("/expense/total/{week}")
    @Operation(summary = "비용 주간 총 금액 조회", description = "비용 주간 총 금액을 조회한다.")
    public ApiResponse<Map<String, Object>> getTotalExpenseWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        Map<String, Object> map = new HashMap<>();
        Long totalAmount = activityService.getExpenseTotalWeek(id, week);
        map.put("totalAmount", totalAmount);
        return ApiResponse.success("비용 주간 총 금액 조회", map);
    }



    @DeleteMapping("/expense")
    @Operation(summary = "비용 내역 삭제", description = "비용 내역을 삭제한다.")
    public ApiResponse<Object> removeExpense(@RequestParam(name = "id") Long id) {
        activityService.removeExpense(id);
        return ApiResponse.success("비용 삭제", true);
    }



    @PostMapping("/feed")
    @Operation(summary = "배식 생성", description = "배식을 저장한다.")
    public ApiResponse<FeedResponse> createFeed(@RequestParam(name = "id") Long id){
        FeedResponse response = activityService.createFeed(id);
        return ApiResponse.success("배식 저장 완료", response);
    }


    @GetMapping("/feed")
    @Operation(summary = "배식 전체 조회", description = "배식 내역을 전체 조회한다.")
    public ApiResponse<List<FeedResponse>> getFeed(@RequestParam(name = "id") Long id){
        List<Feed> response = activityService.getFeed(id);
        List<FeedResponse> responseList = response.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배식 전체 조회", responseList);
    }


    @GetMapping("/feed/{week}")
    @Operation(summary = "배식 주간 조회", description = "배식 내역을 주간 조회한다.")
    public ApiResponse<List<FeedResponse>> getFeedWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Feed> response = activityService.getFeedWeek(id, week);
        List<FeedResponse> responseList = response.stream()
                .map(FeedResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배식 주간 조회", responseList);
    }


    @GetMapping("/feed/check")
    @Operation(summary = "배식 가능 여부 확인", description = "마지막 배식시간이 현재 시각으로 부터 12시간이 경과했는지 확인한다. 가능하면 ture, 불가능하면 false를 return한다.")
    public ApiResponse<Object> checkFeed(@RequestParam(name = "id") Long id){
        Map<String,Object> response = activityService.checkLastFeed(id);
        return ApiResponse.success("배식 가능 여부 확인", response);
    }


    @DeleteMapping("/feed")
    @Operation(summary = "배식 내역 삭제", description = "배식 내역을 삭제한다.")
    public ApiResponse<Object> removeFeed(@RequestParam(name = "id") Long id) {
        activityService.removeFeed(id);
        return ApiResponse.success("배식 삭제", true);
    }


    @PostMapping("/disease/{diseaseName}")
    @Operation(summary = "질병 생성", description = "질병을 저장한다.")
    public ApiResponse<DiseaseResponse> createDisease(@RequestParam(name = "id") Long id, @RequestParam(name = "diseaseName") String diseaseName){
        DiseaseResponse response = activityService.createDisease(id, diseaseName);
        return ApiResponse.success("질병 저장 완료", response);
    }


    @GetMapping("/disease")
    @Operation(summary = "질병 전체 조회", description = "질병 내역을 전체 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDisease(@RequestParam(name = "id") Long id){
        List<Disease> response = activityService.getDisease(id);
        List<DiseaseResponse> responseList = response.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 전체 조회", responseList);
    }


    @GetMapping("/disease/{week}")
    @Operation(summary = "질병 주간 조회", description = "질병 내역을 주간 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDiseaseWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Disease> response = activityService.getDiseaseWeek(id, week);
        List<DiseaseResponse> responseList = response.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 주간 조회", responseList);
    }


    @DeleteMapping("/disease")
    @Operation(summary = "질병 내역 삭제", description = "질병 내역을 삭제한다.")
    public ApiResponse<Object> removeDisease(@RequestParam(name = "id") Long id) {
        activityService.removeDisease(id);
        return ApiResponse.success("질병 삭제", true);
    }


    @PostMapping("/bath")
    @Operation(summary = "목욕 생성", description = "목욕을 저장한다.")
    public ApiResponse<BathResponse> createBath(@RequestParam(name = "id") Long id){
        BathResponse response = activityService.createBath(id);
        return ApiResponse.success("목욕 저장 완료", response);
    }


    @GetMapping("/bath")
    @Operation(summary = "목욕 조회", description = "목욕 내역을 전체 조회한다.")
    public ApiResponse<List<BathResponse>> getBath(@RequestParam(name = "id") Long id){
        List<Bath> response = activityService.getBath(id);
        List<BathResponse> responseList = response.stream()
                .map(BathResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("목욕 조회", responseList);
    }


    @DeleteMapping("/bath")
    @Operation(summary = "목욕 내역 삭제", description = "목욕 내역을 삭제한다.")
    public ApiResponse<Object> removeBath(@RequestParam(name = "id") Long id) {
        activityService.removeBath(id);
        return ApiResponse.success("목욕 삭제", true);
    }


    @GetMapping("/report")
    @Operation(summary = "주간 리포트 전체 조회", description = "주간 리포트 내역을 전체 조회한다.")
    public ApiResponse<List<ReportResponse>> getReport(@RequestParam(name = "id") Long id){
        List<Report> response = activityService.getReport(id);
        List<ReportResponse> responseList = response.stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("주간 리포트 전체 조회", responseList);
    }


    @GetMapping("/report/{week}")
    @Operation(summary = "주간 리포트 주간 조회", description = "주간 리포트 내역을 주간 조회한다.")
    public ApiResponse<List<ReportResponse>> getReport(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Report> response = activityService.getReportWeek(id, week);
        List<ReportResponse> responseList = response.stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("주간 리포트 주간 조회", responseList);
    }


    @PatchMapping ("/report/time")
    @Operation(summary = "총 사용 시간 업데이트", description = "처음 접속 시간과 마지막 접속 시간을 받아서 총 사용 시간을 업데이트한다.")
    public ApiResponse<ReportResponse> updateTotalTimeReport(@RequestParam(name = "id") Long id, @RequestBody UpdateTotalTimeRequest request){
        ReportResponse response = activityService.updateTotalTime(id, request);
        return ApiResponse.success("총 사용 시간 업데이트", response);
    }



    @PostMapping("/affection/{type}")
    @Operation(summary = "애정 지수 생성", description = "애정 지수가 5 상승한다.")
    public ApiResponse<AffectionResponse> createAffection (@RequestParam(name = "id") Long id, @RequestParam(name = "type") AffectionType type){
        AffectionResponse response = activityService.createAffection(id, type, 5);
        return ApiResponse.success("애정 지수 생성", response);
    }


    @GetMapping("/affection")
    @Operation(summary = "애정 지수 전제 조회", description = "애정 지수 내역을 전체 조회한다.")
    public ApiResponse<List<AffectionResponse>> getAffection (@RequestParam(name = "id") Long id){
        List<Affection> response = activityService.getAffection(id);
        List<AffectionResponse> responseList = response.stream().map(AffectionResponse::from).collect(Collectors.toList());
        return ApiResponse.success("애정 지수 전체 조회", responseList);
    }


    @GetMapping("/affection/{week}")
    @Operation(summary = "애정 지수 주간 조회", description = "애정 지수 내역을 주간 조회한다.")
    public ApiResponse<List<AffectionResponse>> getAffection (@RequestParam(name = "id") Long id, @RequestParam(name = "week")int week){
        List<Affection> response = activityService.getAffectionWeek(id, week);
        List<AffectionResponse> responseList = response.stream().map(AffectionResponse::from).collect(Collectors.toList());
        return ApiResponse.success("애정 지수 주간 조회", responseList);
    }


    @PostMapping("/stress/{type}")
    @Operation(summary = "스트레스 지수 생성", description = "스트레스 지수가 1 상승한다.")
    public ApiResponse<StressResponse> createStress (@RequestParam(name = "id") Long id, @RequestParam(name = "type") StressType type){
        StressResponse response = activityService.createStress(id, type, 1);
        return ApiResponse.success("스트레스 지수 생성", response);
    }


    @GetMapping("/stress")
    @Operation(summary = "스트레스 지수 전제 조회", description = "스트레스 지수 내역을 전체 조회한다.")
    public ApiResponse<List<StressResponse>> getStress (@RequestParam(name = "id") Long id){
        List<Stress> response = activityService.getStress(id);
        List<StressResponse> responseList = response.stream().map(StressResponse::from).collect(Collectors.toList());
        return ApiResponse.success("스트레스 지수 전체 조회", responseList);
    }


    @GetMapping("/stress/{week}")
    @Operation(summary = "스트레스 지수 주간 조회", description = "스트레스 지수 내역을 주간 조회한다.")
    public ApiResponse<List<StressResponse>> getStress (@RequestParam(name = "id") Long id, @RequestParam(name = "week")int week){
        List<Stress> response = activityService.getStressWeek(id, week);
        List<StressResponse> responseList = response.stream().map(StressResponse::from).collect(Collectors.toList());
        return ApiResponse.success("스트레스 지수 주간 조회", responseList);
    }

}
