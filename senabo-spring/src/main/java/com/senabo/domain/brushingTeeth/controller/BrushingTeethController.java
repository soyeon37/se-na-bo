package com.senabo.domain.brushingTeeth.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.affection.dto.response.AffectionResponse;
import com.senabo.domain.brushingTeeth.dto.response.BrushingTeethResponse;
import com.senabo.domain.brushingTeeth.entity.BrushingTeeth;
import com.senabo.domain.brushingTeeth.service.BrushingTeethService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/brushing-teeth")
@Tag(name = "BrushingTeeth", description = "BrushingTeeth API Document")
public class BrushingTeethController {

    private final BrushingTeethService brushingTeethService;

    @PostMapping("/save")
    @Operation(summary = "양치 내역 저장", description = "양치 완료 시각을 저장한다.")
    public ApiResponse<BrushingTeethResponse> createBrushingTeeth(@AuthenticationPrincipal UserDetails principal) {
        BrushingTeethResponse response = brushingTeethService.createBrushingTeeth(principal.getUsername());
        return ApiResponse.success("양치 내역 저장 성공", response);
    }


    @GetMapping("/list")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = BrushingTeethResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "양치 내역 전체 조회", description = "양치 내역을 전체 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeeth(@AuthenticationPrincipal UserDetails principal) {
        List<BrushingTeeth> brushingTeeth = brushingTeethService.getBrushingTeeth(principal.getUsername());
        if (brushingTeeth.isEmpty()) return ApiResponse.fail("양치 전체 조회 실패", null);
        List<BrushingTeethResponse> response =  brushingTeeth.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = BrushingTeethResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "양치 내역 주간 조회", description = "양치 내역을 주간 조회한다.")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeethWeek(@AuthenticationPrincipal UserDetails principal, @PathVariable int week) {
        List<BrushingTeeth> brushingTeeth = brushingTeethService.getBrushingTeethWeek(principal.getUsername(), week);
        if (brushingTeeth.isEmpty()) return ApiResponse.fail("양치 "+ week + "주차 조회 실패", null);
      List<BrushingTeethResponse> response = brushingTeeth.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치 " + week + "주차 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "양치 삭제", description = "양치 내역을 삭제한다.")
    public ApiResponse<Object> removeBrushingTeeth(@AuthenticationPrincipal UserDetails principal) {
        brushingTeethService.removeBrushingTeeth(principal.getUsername());
        return ApiResponse.success("양치 삭제 성공", true);
    }

    // 양치 횟수 일주일 3번 제한 체크


}
