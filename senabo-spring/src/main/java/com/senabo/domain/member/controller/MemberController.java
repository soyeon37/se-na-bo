package com.senabo.domain.member.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.dto.request.SignUpRequest;
import com.senabo.domain.member.dto.request.UpdateInfoRequest;
import com.senabo.domain.member.dto.response.BrushingTeethResponse;
import com.senabo.domain.member.dto.response.CommunicationResponse;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.member.dto.response.PoopResponse;
import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Poop;
import com.senabo.domain.member.service.MemberService;
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
@RequestMapping("/api/member")
@Tag(name = "Member", description = "Member API Document")
public class MemberController {
    private final MemberService memberService;
    // 이메일 중복 확인
    @GetMapping("/checkEmail/{email}")
    public ApiResponse<Map<String, Object>> checkEmail(@RequestParam(name = "email") String email){
        log.info("이메일 중복 확인 START");
        boolean duplicateYn = memberService.checkEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("duplicateYn", duplicateYn);
        log.info("이메일 중복 확인 END");
        return ApiResponse.success("이메일 중복 확인", response);
    }

    // 회원가입 create
    @PostMapping("/signUp")
    public ApiResponse<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ApiResponse.success("회원가입 완료", response);
    }
    // 회원탈퇴 delete
    @DeleteMapping("/remove/{id}")
    public ApiResponse<Object> remove(@RequestParam(name = "id") Long id) {
        memberService.removeMember(id);
        return ApiResponse.success("회원탈퇴 완료", true);
    }

    // 로그인
    // 로그아웃
    // Member read
    @GetMapping("/getInfo/{id}")
    public ApiResponse<MemberResponse> getInfo(@RequestParam(name = "id") Long id) {
        MemberResponse response = memberService.getInfo(id);
        return ApiResponse.success("회원정보 조회", response);
    }
    // Member update
    @PatchMapping("/updateInfo/{id}")
    public ApiResponse<MemberResponse> updateInfo(@RequestParam(name = "id") Long id, @RequestBody UpdateInfoRequest request) {
        MemberResponse response = memberService.updateInfo(id, request);
        return ApiResponse.success("회원정보 수정", response);
    }
    // 양치 create
    @PostMapping("/createBrushing/{id}")
    public ApiResponse<BrushingTeethResponse> createBrushingTeeth(@RequestParam(name = "id") Long id){
        BrushingTeethResponse response = memberService.createBrushingTeeth(id);
        return ApiResponse.success("양치질 완료", response);
    }
    // 양치 read
    @GetMapping("/getBrushing/{id}")
    public ApiResponse<List<BrushingTeethResponse>> getBrushingTeeth(@RequestParam(name = "id") Long id){
        List<BrushingTeeth> response = memberService.getBrushingTeeth(id);
        List<BrushingTeethResponse> responseList = response.stream()
                .map(BrushingTeethResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("양치질 조회", responseList);
    }
    // 양치 delete
    @DeleteMapping("/removeBrushing/{id}")
    public ApiResponse<Object> removeBrushingTeeth(@RequestParam(name = "id") Long id) {
        memberService.removeBrushingTeeth(id);
        return ApiResponse.success("양치질 삭제", true);
    }

    // 배변 create
    @PostMapping("/createPoop/{id}")
    public ApiResponse<PoopResponse> createPoop(@RequestParam(name = "id") Long id){
        PoopResponse response = memberService.createPoop(id);
        return ApiResponse.success("배변 완료", response);
    }

    // 배변 read
    @GetMapping("/getPoop/{id}")
    public ApiResponse<List<PoopResponse>> getPoop(@RequestParam(name = "id") Long id){
        List<Poop> response = memberService.getPoop(id);
        List<PoopResponse> responseList = response.stream()
                .map(PoopResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("배변 조회", responseList);
    }
    // 배변 delete
    @DeleteMapping("/removePoop/{id}")
    public ApiResponse<Object> removePoop(@RequestParam(name = "id") Long id) {
        memberService.removePoop(id);
        return ApiResponse.success("배변 삭제", true);
    }
    // 배변 update
//    @PatchMapping("/updatePoop/{id}")
//    public ApiResponse<PoopResponse> updatePoop(@RequestParam(name = "id") Long id){
//        PoopResponse response = memberService.updatePoop(id);
//        return ApiResponse.success("배변 청소 완료", response);
//    }

    // 교감 create
    @PostMapping("/createCommunication/{id}")
    public ApiResponse<CommunicationResponse> createCommunication(@RequestParam(name = "id") Long id){
        CommunicationResponse response = memberService.createCommunication(id);
        return ApiResponse.success("교감 완료", response);
    }
    // 교감 read
    // 산책 create
    // 산책 read
    // 비용 create
    // 비용 read
    // 배식 create
    // 배식 read
    // 질병 create
    // 질병 read
    // 목욕 create
    // 목욕 read
    // 스트레스 update
    // 스트레스 read
    // 전체 정보 read
}