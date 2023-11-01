package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Poop;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PoopResponse(
        Long id,
        Long memberId,
        Boolean cleanYn,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
    public static PoopResponse from(Poop poop) {
        return PoopResponse.builder()
                .id(poop.getId())
                .memberId(poop.getMemberId().getId())
                .cleanYn(poop.getCleanYn())
                .createTime(poop.getCreateTime())
                .updateTime(poop.getUpdateTime())
                .build();
    }
}
