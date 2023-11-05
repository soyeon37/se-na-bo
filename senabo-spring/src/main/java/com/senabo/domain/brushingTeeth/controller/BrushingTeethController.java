package com.senabo.domain.brushingTeeth.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.brushingTeeth.dto.response.BrushingTeethResponse;
import com.senabo.domain.brushingTeeth.entity.BrushingTeeth;
import com.senabo.domain.brushingTeeth.service.BrushingTeethService;
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
@RequestMapping("/api/brushing-teeth")
@Tag(name = "BrushingTeeth", description = "BrushingTeeth API Document")
public class BrushingTeethController {

    private final BrushingTeethService brushingTeethService;

    @PostMapping("/save")
    @Operation(summary = "양치 내역 저장", description = "양치 완료 시각을 저장한다.")
    public ApiResponse<BrushingTeethResponse> createBrushingTeeth(@RequestParam String email) {
        BrushingTeethResponse response = brushingTeethService.createBrushingTeeth(email);
        return ApiResponse.success("양치 내역 저장 성공", response);
    }


    @GetMapping("/list")
    @Operation(summary = "양치 내역 전체 조회", description = "양치 내역을 전체 조회한다.")
    public ApiResponse<Map<String,Object>> getBrushingTeeth(@RequestParam String email) {
        List<BrushingTeeth> brushingTeeth = brushingTeethService.getBrushingTeeth(email);
        if (brushingTeeth.isEmpty()) return ApiResponse.fail("양치 전체 조회 실패", null);
        Map<String ,Object> response = new HashMap<>();
        response.put("brushingTeethList", brushingTeeth.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("양치 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @Operation(summary = "양치 내역 주간 조회", description = "양치 내역을 주간 조회한다.")
    public ApiResponse<Map<String,Object>> getBrushingTeethWeek(@RequestParam String email, @PathVariable int week) {
        List<BrushingTeeth> brushingTeeth = brushingTeethService.getBrushingTeethWeek(email, week);
        if (brushingTeeth.isEmpty()) return ApiResponse.fail("양치 "+ week + "주차 조회 실패", null);
        Map<String ,Object> response = new HashMap<>();
        response.put("brushingTeethList", brushingTeeth.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("양치 " + week + "주차 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "양치 삭제", description = "양치 내역을 삭제한다.")
    public ApiResponse<Object> removeBrushingTeeth(@RequestParam String email) {
        brushingTeethService.removeBrushingTeeth(email);
        return ApiResponse.success("양치 삭제 성공", true);
    }


}
