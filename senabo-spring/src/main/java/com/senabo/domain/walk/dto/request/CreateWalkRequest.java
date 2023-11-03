package com.senabo.domain.walk.dto.request;

import java.time.LocalDateTime;

public record CreateWalkRequest(
        LocalDateTime startTime
) {
}
