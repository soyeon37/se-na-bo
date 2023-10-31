package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Expense;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExpenseResponse(
        Long id,
        Long memberId,
        String item,
        String detail,
        Double amount,
        LocalDateTime dateTime
) {
    public static ExpenseResponse from(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .memberId(expense.getMemberId().getId())
                .item(expense.getItem())
                .detail(expense.getDetail())
                .amount(expense.getAmount())
                .dateTime(expense.getUpdateTime())
                .build();
    }
}
