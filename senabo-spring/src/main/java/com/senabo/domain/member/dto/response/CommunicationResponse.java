package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.ActivityType;
import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Communication;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CommunicationResponse(
        Long id,
        Long memberId,
        ActivityType type,
        LocalDateTime dateTime
) {
    public static CommunicationResponse from(Communication communication) {
        return CommunicationResponse.builder()
                .id(communication.getId())
                .memberId(communication.getMemberId().getId())
                .type(communication.getType())
                .dateTime(communication.getUpdateTime())
                .build();
    }
}
