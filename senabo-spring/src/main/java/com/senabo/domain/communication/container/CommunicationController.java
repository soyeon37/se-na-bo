package com.senabo.domain.communication.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.communication.dto.response.CommunicationResponse;
import com.senabo.domain.communication.entity.Communication;
import com.senabo.domain.communication.service.CommunicationService;
import com.senabo.common.ActivityType;
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
@RequestMapping("/api/communication")
@Tag(name = "Communication", description = "Communication API Document")
public class CommunicationController {

    private final CommunicationService communicationService;

    @PostMapping("/save/{type}")
    @Operation(summary = "교감 내역 저장", description = "교감 내역을 저장한다. WAIT(기다려), SIT(앉아), HAND(손), PAT(쓰다듬기), TUG(터그놀이) 5가지 타입이 있다.")
    public ApiResponse<CommunicationResponse> createCommunication(@RequestParam String email, @RequestParam ActivityType type) {
        if (type == ActivityType.WALK) return ApiResponse.fail("WALK(산책)는 저장 할 수 없습니다.", null);
        CommunicationResponse response = communicationService.createCommunication(email, type);
        return ApiResponse.success("교감 내역 저장 성공", response);
    }


    @GetMapping("/list")
    @Operation(summary = "교감 전체 조회", description = "교감 내역을 전체 조회한다.")
    public ApiResponse<Map<String, Object>>  getCommunication(@RequestParam String email) {
        List<Communication> communication = communicationService.getCommunication(email);
        if (communication.isEmpty()) return ApiResponse.fail("교감 전체 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("communicationList", communication.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("교감 전체 조회 성공", response);
    }

    @GetMapping("/list/{week}")
    @Operation(summary = "교감 주간 조회", description = "교감 내역을 주간 조회한다.")
    public ApiResponse<Map<String, Object>> getCommunication(@RequestParam String email, @RequestParam int week) {
        List<Communication> communication = communicationService.getCommunicationWeek(email, week);
        if (communication.isEmpty()) return ApiResponse.fail("교감 "+ week + "주차 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("communicationList", communication.stream()
                .map(CommunicationResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("교감 " + week + "주차 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "교감 내역 삭제", description = "교감 내역을 삭제한다.")
    public ApiResponse<Object> removeCommunication(@RequestParam String email) {
        communicationService.removeCommunication(email);
        return ApiResponse.success("교감 삭제 성공", true);
    }

}
