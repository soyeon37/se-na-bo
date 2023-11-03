package com.senabo.domain.brushingTeeth.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.brushingTeeth.dto.response.BrushingTeethResponse;
import com.senabo.domain.brushingTeeth.entity.BrushingTeeth;
import com.senabo.domain.brushingTeeth.service.BrushingTeethService;
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
@RequestMapping("/api/brushing-teeth")
@Tag(name = "BrushingTeeth", description = "BrushingTeeth API Document")
public class BrushingTeethController {

    private final BrushingTeethService brushingTeethService;

    @PostMapping("")
    @Operation(summary = "양치 내역 생성", description = "양치 시각을 저장한다.")
    public ApiResponse<BrushingTeethResponse> createBrushingTeeth(@RequestParam(name = "id") Long id) {
        BrushingTeethResponse response = brushingTeethService.createBrushingTeeth(id);
        return ApiResponse.success("양치질 완료", response);
    }


    @GetMapping("")
    @Operation(summary = "양치 내역 전체 조회", description = "양치 내역을 전체 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeeth(@RequestParam(name = "id") Long id) {
        List<BrushingTeeth> response = brushingTeethService.getBrushingTeeth(id);
        List<BrushingTeethResponse> responseList = response.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치질 전체 조회", responseList);
    }


    @GetMapping("/{week}")
    @Operation(summary = "양치 주간 내역 조회", description = "양치 내역을 주간 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeethWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<BrushingTeeth> response = brushingTeethService.getBrushingTeethWeek(id, week);
        List<BrushingTeethResponse> responseList = response.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치질 주간 조회", responseList);
    }


    @DeleteMapping("")
    @Operation(summary = "양치 삭제", description = "양치 내역을 삭제한다.")
    public ApiResponse<Object> removeBrushingTeeth(@RequestParam(name = "id") Long id) {
        brushingTeethService.removeBrushingTeeth(id);
        return ApiResponse.success("양치질 삭제", true);
    }


}
