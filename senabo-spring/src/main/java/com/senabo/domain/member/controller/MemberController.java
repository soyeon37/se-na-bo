package com.senabo.domain.member.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.dto.request.SignUpRequest;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        return ApiResponse.success("회원탈퇴 완료", "");
    }

    // 로그인
    // 로그아웃
    // Member read

    // Member update
    // 양치 create
    // 양치 read
    // 양치 delete
    // 양치 update
    // 배변 create
    // 배변 read
    // 배변 delete
    // 배변 update
    // 교감 create
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