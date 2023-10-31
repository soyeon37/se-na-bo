package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.ActivityType;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CommunicationResponse(
        Long id,
        ActivityType type,
        LocalDateTime dateTime
) {
}
