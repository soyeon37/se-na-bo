package com.senabo.domain.communication.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.communication.dto.response.CommunicationResponse;
import com.senabo.domain.communication.entity.Communication;
import com.senabo.domain.communication.service.CommunicationService;
import com.senabo.domain.communication.entity.ActivityType;
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
@RequestMapping("/api/communication")
@Tag(name = "Communication", description = "Communication API Document")
public class CommunicationController {

    private CommunicationService communicationService;
    
    @PostMapping("/{type}")
    @Operation(summary = "교감 생성", description = "교감 생성 내용을 저장한다. WAIT(기다려), SIT(앉아), HAND(손), PAT(쓰다듬기), TUG(터그놀이) 5가지 타입이 있다.")
    public ApiResponse<CommunicationResponse> createCommunication(@RequestParam(name = "id") Long id, @RequestParam(name = "type") ActivityType type) {
        CommunicationResponse response = communicationService.createCommunication(id, type);
        return ApiResponse.success("교감 완료", response);
    }


    @GetMapping("")
    @Operation(summary = "교감 전체 조회", description = "교감 내역을 전체 조회한다.")
    public ApiResponse<List<CommunicationResponse>> getCommunication(@RequestParam(name = "id") Long id) {
        List<Communication> response = communicationService.getCommunication(id);
        List<CommunicationResponse> responseList = response.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("교감 전체 조회", responseList);
    }

    @GetMapping("/{week}")
    @Operation(summary = "교감 주간 조회", description = "교감 내역을 주간 조회한다.")
    public ApiResponse<List<CommunicationResponse>> getCommunication(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<Communication> response = communicationService.getCommunicationWeek(id, week);
        List<CommunicationResponse> responseList = response.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("교감 주간 조회", responseList);
    }


    @DeleteMapping("/")
    @Operation(summary = "교감 내역 삭제", description = "교감 내역을 삭제한다.")
    public ApiResponse<Object> removeCommunication(@RequestParam(name = "id") Long id) {
        communicationService.removeCommunication(id);
        return ApiResponse.success("교감 삭제", true);
    }

}
