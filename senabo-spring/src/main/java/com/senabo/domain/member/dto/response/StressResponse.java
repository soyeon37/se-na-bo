package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Stress;
import com.senabo.domain.member.entity.StressType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StressResponse(
        Long id,
        Long memberId,
        StressType type,
        String detail,
        int changeAmount,
        int score,
        LocalDateTime dateTime
) {
    public static StressResponse from(Stress stress){
        return StressResponse.builder()
                .id(stress.getId())
                .memberId(stress.getMemberId().getId())
                .type(stress.getType())
                .detail(stress.getDetail())
                .changeAmount(stress.getChangeAmount())
                .score(stress.getScore())
                .dateTime(stress.getUpdateTime())
                .build();
    }
}
