package com.senabo.domain.report.container;

import com.google.protobuf.Api;
import com.senabo.common.api.ApiResponse;
import com.senabo.domain.report.dto.request.UpdateTotalTimeRequest;
import com.senabo.domain.report.dto.response.ReportResponse;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
@Tag(name = "Report", description = "Report API Document")
public class ReportController {
    
    private final ReportService reportService;
    
    @GetMapping("/list")
    @Operation(summary = "주간 리포트 전체 조회", description = "주간 리포트 내역을 전체 조회한다.")
    public ApiResponse<Map<String, Object>> getReport(@RequestParam String email) {
        List<Report> report = reportService.getReport(email);
        if (report.isEmpty()) return ApiResponse.fail("주간 리포트 전체 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("reportList", report.stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("주간 리포트 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @Operation(summary = "주간 리포트 주간 조회", description = "주간 리포트 내역을 주간 조회한다.")
    public ApiResponse<ReportResponse> getReportWeek(@RequestParam String email, @RequestParam int week) {
        Optional<Report> response = reportService.getReportWeek(email, week);
        if (response.isEmpty()) return ApiResponse.fail("주간 리포트 " + week + "주차 조회 실패", null);
        return ApiResponse.success("주간 리포트 " + week + "주차 조회 성공", ReportResponse.from(response.get()));
    }

    @PatchMapping("/time")
    @Operation(summary = "총 사용 시간 업데이트", description = "처음 접속 시간과 마지막 접속 시간을 받아서 총 사용 시간을 업데이트한다.")
    public ApiResponse<ReportResponse> updateTotalTimeReport(@RequestParam String email, @RequestBody UpdateTotalTimeRequest request) {
        ReportResponse response = reportService.updateTotalTime(email, request);
        return ApiResponse.success("총 사용 시간 업데이트 성공", response);
    }


}
