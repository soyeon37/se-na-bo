package com.senabo.domain.disease.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.disease.dto.response.DiseaseResponse;
import com.senabo.domain.disease.entity.Disease;
import com.senabo.domain.disease.service.DiseaseService;
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
@RequestMapping("/api/disease")
@Tag(name = "Disease", description = "Disease API Document")
public class DiseaseController {
    
    private final DiseaseService diseaseService;
    
    @PostMapping("/disease/{diseaseName}")
    @Operation(summary = "질병 생성", description = "질병을 저장한다.")
    public ApiResponse<DiseaseResponse> createDisease(@RequestParam(name = "id") Long id, @RequestParam(name = "diseaseName") String diseaseName) {
        DiseaseResponse response = diseaseService.createDisease(id, diseaseName);
        return ApiResponse.success("질병 저장 완료", response);
    }


    @GetMapping("/disease")
    @Operation(summary = "질병 전체 조회", description = "질병 내역을 전체 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDisease(@RequestParam(name = "id") Long id) {
        List<Disease> response = diseaseService.getDisease(id);
        List<DiseaseResponse> responseList = response.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 전체 조회", responseList);
    }


    @GetMapping("/disease/{week}")
    @Operation(summary = "질병 주간 조회", description = "질병 내역을 주간 조회한다.")
    public ApiResponse<List<DiseaseResponse>> getDiseaseWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<Disease> response = diseaseService.getDiseaseWeek(id, week);
        List<DiseaseResponse> responseList = response.stream()
                .map(DiseaseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("질병 주간 조회", responseList);
    }


    @DeleteMapping("/disease")
    @Operation(summary = "질병 내역 삭제", description = "질병 내역을 삭제한다.")
    public ApiResponse<Object> removeDisease(@RequestParam(name = "id") Long id) {
        diseaseService.removeDisease(id);
        return ApiResponse.success("질병 삭제", true);
    }
}
