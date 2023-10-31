package com.senabo.domain.member.dto.request;

public record CreateExpenseRequest(
        String item,
        String detail,
        Double amount
) {
}
