package com.senabo.domain.poop.controller;


import com.senabo.common.api.ApiResponse;
import com.senabo.domain.expense.dto.response.ExpenseResponse;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.poop.dto.response.PoopResponse;
import com.senabo.domain.poop.entity.Poop;
import com.senabo.domain.poop.service.PoopService;
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
@RequestMapping("/poop")
@Tag(name = "Poop", description = "Poop API Document")
public class PoopController {

    private final PoopService poopService;
    
    @PostMapping("/save")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배변 생성 시각을 저장한다.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = PoopResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "배변 생성", description = "배변 생성 시각을 저장한다.")
    public ApiResponse<PoopResponse> createPoop(@AuthenticationPrincipal UserDetails principal){
        PoopResponse response = poopService.createPoop(principal.getUsername());
        return ApiResponse.success("배변 저장 성공", response);
    }


    @GetMapping("/list")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = PoopResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "배변 전체 조회", description = "배변 내역을 전체 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoop(@AuthenticationPrincipal UserDetails principal){
        List<Poop> poop = poopService.getPoop(principal.getUsername());
        if(poop.isEmpty()) return ApiResponse.fail("배변 전체 조회 실패", null);
        List<PoopResponse> response = poop.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = PoopResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "배변 주간 조회", description = "배변 내역을 주간 조회한다.")
    public ApiResponse<List<PoopResponse>> getPoopWeek(@AuthenticationPrincipal UserDetails principal, @PathVariable int week){
        List<Poop> poop = poopService.getPoopWeek(principal.getUsername(), week);
        if(poop.isEmpty()) return ApiResponse.fail("배변 " + week + "주차 조회 실패", null);
        List<PoopResponse> response = poop.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 " + week + "주차 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "배변 내역 삭제", description = "배변 내역을 삭제한다.")
    public ApiResponse<Object> removePoop(@AuthenticationPrincipal UserDetails principal) {
        poopService.removePoop(principal.getUsername());
        return ApiResponse.success("배변 삭제 성공", true);
    }


    @PatchMapping("/clean")
    @Operation(summary = "배변 청소 완료", description = "배변 청소 여부(cleanYn)를 true로 수정한다.")
    public ApiResponse<PoopResponse> updatePoop(@AuthenticationPrincipal UserDetails principal){
        PoopResponse response = poopService.updatePoop(principal.getUsername());
        return ApiResponse.success("배변 청소 성공", response);
    }
}
