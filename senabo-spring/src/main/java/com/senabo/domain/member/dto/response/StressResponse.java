package com.senabo.domain.member.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StressResponse(
        Long id,
        Long memberId,
        int poopScore,
        int brushingTeethScore,
        int walkScore,
        int feedScore,
        int showerScore,
        LocalDateTime dateTime
) {
}
