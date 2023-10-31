package com.senabo.domain.member.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WalkResponse(
        Long id,
        Double distance,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime dateTime
) {
}
