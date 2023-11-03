package com.senabo.domain.affection.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.affection.entity.AffectionType;
import com.senabo.domain.affection.service.AffectionService;
import com.senabo.domain.affection.dto.response.AffectionResponse;
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
@RequestMapping("/api/affection")
@Tag(name = "Affection", description = "Affection API Document")
public class AffectionController {
    private final AffectionService affectionService;
    
    @PostMapping("/{type}")
    @Operation(summary = "애정 지수 생성", description = "애정 지수가 5 상승한다.")
    public ApiResponse<AffectionResponse> createAffection (@RequestParam(name = "id") Long id, @RequestParam(name = "type") AffectionType type){
        AffectionResponse response = affectionService.createAffection(id, type, 5);
        return ApiResponse.success("애정 지수 생성", response);
    }


    @GetMapping("")
    @Operation(summary = "애정 지수 전제 조회", description = "애정 지수 내역을 전체 조회한다.")
    public ApiResponse<List<AffectionResponse>> getAffection (@RequestParam(name = "id") Long id){
        List<Affection> response = affectionService.getAffection(id);
        List<AffectionResponse> responseList = response.stream().map(AffectionResponse::from).collect(Collectors.toList());
        return ApiResponse.success("애정 지수 전체 조회", responseList);
    }


    @GetMapping("/{week}")
    @Operation(summary = "애정 지수 주간 조회", description = "애정 지수 내역을 주간 조회한다.")
    public ApiResponse<List<AffectionResponse>> getAffection (@RequestParam(name = "id") Long id, @RequestParam(name = "week")int week){
        List<Affection> response = affectionService.getAffectionWeek(id, week);
        List<AffectionResponse> responseList = response.stream().map(AffectionResponse::from).collect(Collectors.toList());
        return ApiResponse.success("애정 지수 주간 조회", responseList);
    }

}
