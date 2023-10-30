package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.SNSType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record MemberResponse(
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
        LocalDateTime enterTime
        ) {
}
