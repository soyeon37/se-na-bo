package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Disease;
import com.senabo.domain.member.entity.Expense;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findByMemberId(Member memberId);

    List<Expense> deleteByMemberId(Member memberId);

    @Query("select e from Expense e where e.memberId = ?1 and e.updateTime <= ?2 and e.createTime >= ?3 ")
    List<Expense> findExpenseWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.memberId = ?1")
    Long getTotalAmount(Member member);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.memberId = ?1 and e.updateTime <= ?2 and e.createTime >= ?3 ")
    Long getTotalAmountWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
