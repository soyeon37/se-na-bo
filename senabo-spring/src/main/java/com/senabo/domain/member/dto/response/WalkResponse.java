package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Walk;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WalkResponse(
        Long id,
        Long memberId,
        Double distance,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime dateTime
) {
    public static WalkResponse from(Walk walk){
        return WalkResponse.builder()
                .id(walk.getId())
                .memberId(walk.getMemberId().getId())
                .startTime(walk.getStartTime())
                .endTime(walk.getEndTime())
                .distance(walk.getDistance())
                .dateTime(walk.getUpdateTime())
                .build();
    }

}
