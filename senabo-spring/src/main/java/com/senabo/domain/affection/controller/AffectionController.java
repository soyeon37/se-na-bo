package com.senabo.domain.affection.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.affection.dto.request.AddAffectionRequest;
import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.affection.service.AffectionService;
import com.senabo.domain.affection.dto.response.AffectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/affection")
@Tag(name = "Affection", description = "Affection API Document")
public class AffectionController {
    private final AffectionService affectionService;

    @PostMapping("/save")
    @Operation(summary = "애정 지수 증가", description = "애정 지수가 입력한 수만큼 변화한다.")
    public ApiResponse<AffectionResponse> addAffection(@RequestParam String email, @RequestBody AddAffectionRequest request) {
        AffectionResponse response = affectionService.createAffection(email, request.type(), request.changeAmount());
        return ApiResponse.success("애정 지수 " + request.changeAmount() + " 변화 성공", response);
    }


    @GetMapping("/list")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = AffectionResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
        }
    )
    @Operation(summary = "애정 지수 전체 조회", description = "애정 지수 내역을 전체 조회한다.")
    public ApiResponse<List<AffectionResponse>> getAffection(@RequestParam String email) {
        List<Affection> affection = affectionService.getAffection(email);
        if (affection.isEmpty()) return ApiResponse.fail("애정 지수 전체 조회 실패", null);
        List<AffectionResponse> reponse = affection.stream()
                .map(AffectionResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("애정 지수 전체 조회 성공", reponse);
    }


    @GetMapping("/list/{week}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = AffectionResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "애정 지수 주간 조회", description = "애정 지수 내역을 주간 조회한다.")
    public ApiResponse<List<AffectionResponse>> getAffectionWeek(@RequestParam String email, @PathVariable int week) {
        List<Affection> affection = affectionService.getAffectionWeek(email, week);
        if (affection.isEmpty()) return ApiResponse.fail("애정 지수 " + week + "주차 조회 실패", null);
        List<AffectionResponse> response = affection.stream().map(AffectionResponse::from).collect(Collectors.toList());
        return ApiResponse.success("애정 지수 " + week + "주차 조회 성공", response);
    }

}
