package com.senabo.domain.member.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.dto.request.*;
import com.senabo.domain.member.dto.response.*;
import com.senabo.domain.member.entity.*;
import com.senabo.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/member")
@Tag(name = "Member", description = "Member API Document")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check/email")
    @Operation(summary = "이메일 중복 확인", description = "이미 저장된 이메일인지 중복확인 한다.")
    public ApiResponse<Map<String, Object>> checkEmail(@RequestParam(name = "email") String email){
        boolean duplicateYn = memberService.checkEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("duplicateYn", duplicateYn);
        return ApiResponse.success("이메일 중복 확인", response);
    }


    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "토큰과 정보를 받아 회원가입을 한다.")
    public ApiResponse<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ApiResponse.success("회원가입 완료", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "회원 탈퇴", description = "회원정보를 전부 삭제한다.")
    public ApiResponse<Object> remove(@RequestParam(name = "id") Long id) {
        memberService.removeMember(id);
        return ApiResponse.success("회원탈퇴 완료", true);
    }

    // 로그인

    // 로그아웃


    @GetMapping("/get")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회한다.")
    public ApiResponse<MemberResponse> getInfo(@RequestParam(name = "id") Long id) {
        MemberResponse response = memberService.getInfo(id);
        return ApiResponse.success("회원정보 조회", response);
    }


    @PatchMapping("/update")
    @Operation(summary = "회원 정보 수정", description = "강아지 이름, 성별, 종, 위도, 경도를 수정한다.")
    public ApiResponse<MemberResponse> updateInfo(@RequestParam(name = "id") Long id, @RequestBody UpdateInfoRequest request) {
        MemberResponse response = memberService.updateInfo(id, request);
        return ApiResponse.success("회원정보 수정", response);
    }


}