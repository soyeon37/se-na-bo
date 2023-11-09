package com.senabo.domain.member.dto.response;


//import com.senabo.config.security.jwt.TokenInfo;
import com.senabo.config.security.jwt.TokenInfo;
import com.senabo.domain.member.entity.Sex;
import com.senabo.domain.member.entity.Species;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class FirebaseAuthResponse {
    boolean firebaseAuthStatus;
    boolean isMember;
    SignInResponse signInResponse;

    @Getter
    @Builder
    public static class SignInResponse {
        Long id;
        String dogName;
        String email;
        Species species;
        Sex sex;
        BigDecimal houseLatitude;
        BigDecimal houseLongitude;
        int affection;
        int stressLevel;
        int totalTime;
        LocalDateTime exitTime;
        LocalDateTime enterTime;
        LocalDateTime createTime;
        LocalDateTime updateTime;
        TokenInfo token;
    }

    // 파이어베이스 인증 오류
    public FirebaseAuthResponse() {
        firebaseAuthStatus = false;
        isMember = false;
    }

    // 파이어베이스 인증 성공 및 회원인 경우
    public FirebaseAuthResponse(boolean isMember, SignInResponse signInResponse) {
        firebaseAuthStatus = true;
        this.isMember = isMember;
        this.signInResponse = signInResponse;
    }

    // 파이어베이스 인증 성공 및 회원이 아닌 경우
    public FirebaseAuthResponse(boolean isMember) {
        firebaseAuthStatus = true;
        this.isMember = isMember;
    }
}