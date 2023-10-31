package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Poop;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PoopResponse(
        Long id,
        Boolean cleanYn,
        LocalDateTime dateTime
) {
    public static PoopResponse from(Poop poop) {
        return PoopResponse.builder()
                .id(poop.getId())
                .cleanYn(poop.getCleanYn())
                .dateTime(poop.getUpdateTime())
                .build();
    }
}
