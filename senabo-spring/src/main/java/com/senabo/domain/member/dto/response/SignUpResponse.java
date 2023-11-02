package com.senabo.domain.member.dto.response;

import com.senabo.config.security.jwt.TokenInfo;
import com.senabo.domain.member.entity.SNSType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record SignUpResponse(
        Long id,
        String name,
        String email,
        String species,
        String sex,
        int affection,
        int stressLevel,
        BigDecimal houseLatitude,
        BigDecimal houseLogitude,
        SNSType snsType,
        int totalTime,
        LocalDateTime exitTime,
        LocalDateTime enterTime,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        TokenInfo token) {
}
