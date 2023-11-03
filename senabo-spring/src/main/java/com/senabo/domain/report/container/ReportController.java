package com.senabo.domain.report.container;

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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
@Tag(name = "Report", description = "Report API Document")
public class ReportController {
    
    private final ReportService reportService;
    
    @GetMapping("/report")
    @Operation(summary = "주간 리포트 전체 조회", description = "주간 리포트 내역을 전체 조회한다.")
    public ApiResponse<List<ReportResponse>> getReport(@RequestParam(name = "id") Long id) {
        List<Report> response = reportService.getReport(id);
        List<ReportResponse> responseList = response.stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("주간 리포트 전체 조회", responseList);
    }


    @GetMapping("/report/{week}")
    @Operation(summary = "주간 리포트 주간 조회", description = "주간 리포트 내역을 주간 조회한다.")
    public ApiResponse<List<ReportResponse>> getReport(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<Report> response = reportService.getReportWeek(id, week);
        List<ReportResponse> responseList = response.stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("주간 리포트 주간 조회", responseList);
    }

    @PatchMapping("/report/time")
    @Operation(summary = "총 사용 시간 업데이트", description = "처음 접속 시간과 마지막 접속 시간을 받아서 총 사용 시간을 업데이트한다.")
    public ApiResponse<ReportResponse> updateTotalTimeReport(@RequestParam(name = "id") Long id, @RequestBody UpdateTotalTimeRequest request) {
        ReportResponse response = reportService.updateTotalTime(id, request);
        return ApiResponse.success("총 사용 시간 업데이트", response);
    }


}
