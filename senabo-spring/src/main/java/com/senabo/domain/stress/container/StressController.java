package com.senabo.domain.stress.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.stress.dto.response.StressResponse;
import com.senabo.domain.stress.entity.Stress;
import com.senabo.domain.stress.entity.StressType;
import com.senabo.domain.stress.service.StressService;
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
@RequestMapping("/api/stress")
@Tag(name = "Stress", description = "Stress API Document")
public class StressController {
    private final StressService stressService;




    @PostMapping("/stress/{type}")
    @Operation(summary = "스트레스 지수 생성", description = "스트레스 지수가 1 상승한다.")
    public ApiResponse<StressResponse> createStress(@RequestParam(name = "id") Long id, @RequestParam(name = "type") StressType type) {
        StressResponse response = stressService.createStress(id, type, 1);
        return ApiResponse.success("스트레스 지수 생성", response);
    }


    @GetMapping("/stress")
    @Operation(summary = "스트레스 지수 전제 조회", description = "스트레스 지수 내역을 전체 조회한다.")
    public ApiResponse<List<StressResponse>> getStress(@RequestParam(name = "id") Long id) {
        List<Stress> response = stressService.getStress(id);
        List<StressResponse> responseList = response.stream().map(StressResponse::from).collect(Collectors.toList());
        return ApiResponse.success("스트레스 지수 전체 조회", responseList);
    }


    @GetMapping("/stress/{week}")
    @Operation(summary = "스트레스 지수 주간 조회", description = "스트레스 지수 내역을 주간 조회한다.")
    public ApiResponse<List<StressResponse>> getStress(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<Stress> response = stressService.getStressWeek(id, week);
        List<StressResponse> responseList = response.stream().map(StressResponse::from).collect(Collectors.toList());
        return ApiResponse.success("스트레스 지수 주간 조회", responseList);
    }
}
