package com.senabo.domain.walk.dto.request;

import java.time.LocalDateTime;

public record UpdateWalkRequest(
        LocalDateTime endTime,
        Double distnace
) {
}
