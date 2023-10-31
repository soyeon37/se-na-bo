package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BrushingTeethResponse(
        Long id,
        Long memberId,
        LocalDateTime dateTime
) {
    public static BrushingTeethResponse from(BrushingTeeth brushingTeeth) {
        return BrushingTeethResponse.builder()
                .id(brushingTeeth.getId())
                .memberId(brushingTeeth.getMemberId().getId())
                .dateTime(brushingTeeth.getUpdateTime())
                .build();
    }

}
