package com.senabo.domain.member.dto.request;

import java.time.LocalDateTime;

public record UpdateTotalTimeRequest(
        LocalDateTime startTime,
        LocalDateTime endTime

) {
}
