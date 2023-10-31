package com.senabo.domain.member.dto.request;

import java.time.LocalDateTime;

public record UpdateWalkRequest(
        LocalDateTime endTime,
        Double distnace
) {
}
