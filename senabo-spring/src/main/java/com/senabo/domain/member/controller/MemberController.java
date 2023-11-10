package com.senabo.domain.member.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.expense.dto.response.ExpenseResponse;
import com.senabo.domain.member.dto.request.*;
import com.senabo.domain.member.dto.response.*;
import com.senabo.domain.member.entity.*;
import com.senabo.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Tag(name = "Member", description = "Member API Document")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "이메일 중복 확인", description = "이미 저장된 이메일인지 중복확인 한다.")
    public ApiResponse<CheckEmailResponse> checkEmail(@RequestParam String email) {
        CheckEmailResponse response = memberService.checkEmail(email);
        return ApiResponse.success("이메일 중복 확인 성공", response);
    }

    @PostMapping("/sign-up")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 정보를 받아 회원가입을 한다.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "회원가입", description = "토큰과 정보를 받아 회원가입을 한다.")
    public ApiResponse<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);


        return ApiResponse.success("회원가입 성공", response);
    }


    @DeleteMapping("/remove")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자를 삭제한다.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "회원 탈퇴", description = "회원정보를 전부 삭제한다.")
    public ApiResponse<Object> remove(@RequestBody SignOutRequest request, @AuthenticationPrincipal UserDetails principal) {
        memberService.removeMember(principal.getUsername(), request);
        return ApiResponse.success("회원탈퇴 성공", true);
    }

    // 로그인
    @PostMapping("/sign-in")
    @Operation(summary = "회원 로그인", description = "구글 OAuth로 로그인을 한다.")
    public FirebaseAuthResponse firebaseToken(@RequestBody FirebaseAuthRequest firebaseAuthRequest) {
        return memberService.signIn(firebaseAuthRequest);
    }


    // 로그아웃
    @PostMapping("/sign-out")
    @Operation(summary = "로그아웃", description = "로그아웃을 한다.")
    public ApiResponse<Object> signOut(@RequestBody SignOutRequest request, @AuthenticationPrincipal UserDetails principal) {
        memberService.signOut(request, principal.getUsername());
        return ApiResponse.success("로그아웃 성공", null);
    }


    @GetMapping("/get")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 정보를 가져온다.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회한다.")
    public ApiResponse<MemberResponse> getInfo(@AuthenticationPrincipal UserDetails principal) {
        MemberResponse response = memberService.getInfo(principal.getUsername());
        return ApiResponse.success("회원정보 조회 성공", response);
    }

    @PutMapping("/update")
    @Operation(summary = "회원 정보 수정", description = "강아지 이름, 성별, 종, 위도, 경도를 수정한다.")
    public ApiResponse<MemberResponse> updateInfo(@AuthenticationPrincipal UserDetails principal, @RequestBody UpdateInfoRequest request) {
        MemberResponse response = memberService.updateInfo(principal.getUsername(), request);
        return ApiResponse.success("회원정보 수정 성공", response);
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "만료된 토큰을 받아서 재발급한다.")
    public ApiResponse<ReIssueResponse> reissue(@RequestBody ReIssueRequest request) {
        return ApiResponse.success("토큰 재발급 성공", memberService.reissue(request.refreshToken(), SecurityContextHolder.getContext().getAuthentication()));
    }
}