package com.senabo.domain.disease.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.disease.dto.response.DiseaseResponse;
import com.senabo.domain.disease.entity.Disease;
import com.senabo.domain.disease.service.DiseaseService;
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
@RequestMapping("/api/disease")
@Tag(name = "Disease", description = "Disease API Document")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @PostMapping("/save/{diseaseName}")
    @Operation(summary = "질병 내역 저장", description = "질병 내역을 저장한다.")
    public ApiResponse<DiseaseResponse> createDisease(@RequestParam String email, @PathVariable String diseaseName) {
        DiseaseResponse response = diseaseService.createDisease(email, diseaseName);
        return ApiResponse.success("질병 저장 완료", response);
    }


    @GetMapping("/list")
    @Operation(summary = "질병 전체 조회", description = "질병 내역을 전체 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDisease(@RequestParam String email) {
        List<Disease> disease = diseaseService.getDisease(email);
        if (disease.isEmpty()) return ApiResponse.fail("질병 전체 조회 실패", null);
        List<DiseaseResponse> response = disease.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @Operation(summary = "질병 주간 조회", description = "질병 내역을 주간 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDiseaseWeek(@RequestParam String email, @PathVariable  int week) {
        List<Disease> disease = diseaseService.getDiseaseWeek(email, week);
        if (disease.isEmpty()) return ApiResponse.fail("질병 " + week + "주차 조회 실패", null);
        List<DiseaseResponse> response = disease.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 " + week + "주차 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "질병 내역 삭제", description = "질병 내역을 삭제한다.")
    public ApiResponse<Object> removeDisease(@RequestParam String email) {
        diseaseService.removeDisease(email);
        return ApiResponse.success("질병 삭제 성공", true);
    }
}
