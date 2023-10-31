package com.senabo.domain.member.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExpenseResponse(
        Long id,
        String item,
        String detail,
        Double amount,
        LocalDateTime dateTime
) {
}
