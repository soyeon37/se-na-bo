package com.senabo.domain.member.dto.response;

//import com.senabo.config.security.jwt.TokenInfo;
import com.senabo.domain.member.entity.Sex;
import com.senabo.domain.member.entity.Species;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record SignUpResponse(
        Long id,
        String dogName,
        String email,
        Species species,
        Sex sex,
        int affection,
        int stressLevel,
        BigDecimal houseLatitude,
        BigDecimal houseLongitude,
        int totalTime,
        LocalDateTime exitTime,
        LocalDateTime enterTime,
        LocalDateTime createTime,
        LocalDateTime updateTime
//        TokenInfo token
) {
}
