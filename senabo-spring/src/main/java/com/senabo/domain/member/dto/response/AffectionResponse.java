package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Affection;
import com.senabo.domain.member.entity.AffectionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AffectionResponse(
        Long id,
        Long memberId,
        AffectionType type,
        int changeAmount,
        int score,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
    public static AffectionResponse from(Affection affection){
        return AffectionResponse.builder()
                .id(affection.getId())
                .memberId(affection.getMemberId().getId())
                .type(affection.getType())
                .changeAmount(affection.getChangeAmount())
                .score(affection.getScore())
                .createTime(affection.getCreateTime())
                .updateTime(affection.getUpdateTime())
                .build();
    }
}

