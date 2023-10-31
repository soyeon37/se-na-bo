package com.senabo.domain.member.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record DiseaseResponse(
        Long id,
        String diseaseName,
        LocalDateTime dateTime
) {
}
