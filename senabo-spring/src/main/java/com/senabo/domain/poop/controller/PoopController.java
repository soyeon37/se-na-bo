package com.senabo.domain.poop.controller;


import com.senabo.common.api.ApiResponse;
import com.senabo.domain.poop.dto.response.PoopResponse;
import com.senabo.domain.poop.entity.Poop;
import com.senabo.domain.poop.service.PoopService;
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
@RequestMapping("/api/poop")
@Tag(name = "Poop", description = "Poop API Document")
public class PoopController {
    private final PoopService poopService;
    
    @PostMapping("/save")
    @Operation(summary = "배변 생성", description = "배변 생성 시각을 저장한다.")
    public ApiResponse<PoopResponse> createPoop(@RequestParam String email){
        PoopResponse response = poopService.createPoop(email);
        return ApiResponse.success("배변 저장 성공", response);
    }


    @GetMapping("/list")
    @Operation(summary = "배변 전체 조회", description = "배변 내역을 전체 조회한다.")
    public ApiResponse<Map<String,Object>> getPoop(@RequestParam String email){
        List<Poop> poop = poopService.getPoop(email);
        if(poop.isEmpty()) return ApiResponse.fail("배변 전체 조회 실패", null);
        Map<String,Object> response = new HashMap<>();
        response.put("poopList", poop.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("배변 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @Operation(summary = "배변 주간 조회", description = "배변 내역을 주간 조회한다.")
    public ApiResponse<Map<String,Object>> getPoopWeek(@RequestParam String email, @RequestParam int week){
        List<Poop> poop = poopService.getPoopWeek(email, week);
        if(poop.isEmpty()) return ApiResponse.fail("배변 " + week + "주차 조회 실패", null);
        Map<String,Object> response = new HashMap<>();
        response.put("poopList", poop.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("배변 " + week + "주차 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "배변 내역 삭제", description = "배변 내역을 삭제한다.")
    public ApiResponse<Object> removePoop(@RequestParam String email) {
        poopService.removePoop(email);
        return ApiResponse.success("배변 삭제 성공", true);
    }


    @PatchMapping("/clean")
    @Operation(summary = "배변 청소 완료", description = "배변 청소 여부(cleanYn)를 true로 수정한다.")
    public ApiResponse<PoopResponse> updatePoop(@RequestParam String email){
        PoopResponse response = poopService.updatePoop(email);
        return ApiResponse.success("배변 청소 성공", response);
    }
}
