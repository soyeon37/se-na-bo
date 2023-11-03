package com.senabo.domain.bath.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.bath.dto.response.BathResponse;
import com.senabo.domain.bath.entity.Bath;
import com.senabo.domain.bath.service.BathService;
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
@RequestMapping("/api/bath")
@Tag(name = "Bath", description = "Bath API Document")
public class BathController {
    
    private final BathService bathService;

    @PostMapping("/bath")
    @Operation(summary = "목욕 생성", description = "목욕을 저장한다.")
    public ApiResponse<BathResponse> createBath(@RequestParam(name = "id") Long id){
        BathResponse response = bathService.createBath(id);
        return ApiResponse.success("목욕 저장 완료", response);
    }


    @GetMapping("/bath")
    @Operation(summary = "목욕 조회", description = "목욕 내역을 전체 조회한다.")
    public ApiResponse<List<BathResponse>> getBath(@RequestParam(name = "id") Long id){
        List<Bath> response = bathService.getBath(id);
        List<BathResponse> responseList = response.stream()
                .map(BathResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("목욕 조회", responseList);
    }


    @DeleteMapping("/bath")
    @Operation(summary = "목욕 내역 삭제", description = "목욕 내역을 삭제한다.")
    public ApiResponse<Object> removeBath(@RequestParam(name = "id") Long id) {
        bathService.removeBath(id);
        return ApiResponse.success("목욕 삭제", true);
    }
}
