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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/poop")
@Tag(name = "Poop", description = "Poop API Document")
public class PoopController {
    private final PoopService poopService;
    
    @PostMapping("/poop")
    @Operation(summary = "배변 생성", description = "배변 생성 시각을 저장한다.")
    public ApiResponse<PoopResponse> createPoop(@RequestParam(name = "id") Long id){
        PoopResponse response = poopService.createPoop(id);
        return ApiResponse.success("배변 완료", response);
    }


    @GetMapping("/poop")
    @Operation(summary = "배변 전체 조회", description = "배변 내역을 전체 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoop(@RequestParam(name = "id") Long id){
        List<Poop> response = poopService.getPoop(id);
        List<PoopResponse> responseList = response.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 전체 조회", responseList);
    }


    @GetMapping("/poop/{week}")
    @Operation(summary = "배변 주간 조회", description = "배변 내역을 주간 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoopWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week){
        List<Poop> response = poopService
.getPoopWeek(id, week);
        List<PoopResponse> responseList = response.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 주간 조회", responseList);
    }


    @DeleteMapping("/poop")
    @Operation(summary = "배변 내역 삭제", description = "배변 내역을 삭제한다.")
    public ApiResponse<Object> removePoop(@RequestParam(name = "id") Long id) {
        poopService.removePoop(id);
        return ApiResponse.success("배변 삭제", true);
    }


    @PatchMapping("/poop")
    @Operation(summary = "배변 청소 완료", description = "배변 청소 여부(cleanYn)를 true로 수정한다.")
    public ApiResponse<PoopResponse> updatePoop(@RequestParam(name = "id") Long id){
        PoopResponse response = poopService.updatePoop(id);
        return ApiResponse.success("배변 청소 완료", response);
    }
}
