package com.senabo.domain.member.dto.response;
import com.senabo.config.security.jwt.TokenInfo;
import com.senabo.domain.member.entity.Sex;
import com.senabo.domain.member.entity.Species;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SignInResponse {

    boolean isMember;
    SignInInfoResponse signInInfoResponse;

    @Getter
    @Builder
    public static class SignInInfoResponse {
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


    // 회원인 경우
    public SignInResponse(boolean isMember, SignInInfoResponse signInResponse) {
        this.isMember = isMember;
        this.signInInfoResponse = signInResponse;
    }

    // 회원이 아닌 경우
    public SignInResponse(boolean isMember) {
        this.isMember = isMember;
    }
}