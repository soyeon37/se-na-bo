package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Bath;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BathResponse(
        Long id,
        Long memberId,

        LocalDateTime dateTime
) {

    public static BathResponse from(Bath bath) {
        return BathResponse.builder()
                .id(bath.getId())
                .memberId(bath.getMemberId().getId())
                .dateTime(bath.getUpdateTime())
                .build();
    }
}
