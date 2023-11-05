package com.senabo.domain.expense.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.senabo.domain.expense.entity.Expense;
import com.senabo.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import static com.senabo.domain.communication.entity.QCommunication.communication;
import static com.senabo.domain.expense.entity.QExpense.expense;

@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Expense> findByMemberId(Member member) {
        return queryFactory
                .selectFrom(expense)
                .where(expense.memberId.eq(member))
                .orderBy(expense.createTime.desc())
                .fetch();
    }

    @Override
    public List<Expense> findExpenseWeek(Member member, LocalDateTime endTime, LocalDateTime startTime) {
        return queryFactory.selectFrom(expense)
                .where(
                        expense.memberId.eq(member),
                        expense.updateTime.loe(endTime),
                        expense.createTime.goe(startTime)
                )
                .orderBy(expense.createTime.desc())
                .fetch();
    }

    @Override
    public Double getTotalAmount(Member member) {
        return queryFactory.select(expense.amount.sum())
                .from(expense)
                .where(expense.memberId.eq(member))
                .fetchOne();
    }

    @Override
    public Double getTotalAmountWeek(Member member, LocalDateTime endTime, LocalDateTime startTime) {
        return queryFactory.select(expense.amount.sum())
                .from(expense)
                .where(
                        expense.memberId.eq(member),
                        expense.updateTime.loe(endTime),
                        expense.createTime.goe(startTime)
                )
                .fetchOne();
    }
}
