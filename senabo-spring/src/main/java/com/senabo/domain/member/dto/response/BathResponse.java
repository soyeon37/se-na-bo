package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Bath;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BathResponse(
        Long id,
        Long memberId,

        LocalDateTime createTime,
        LocalDateTime updateTime
) {

    public static BathResponse from(Bath bath) {
        return BathResponse.builder()
                .id(bath.getId())
                .memberId(bath.getMemberId().getId())
                .createTime(bath.getCreateTime())
                .updateTime(bath.getUpdateTime())
                .build();
    }
}
