package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Disease;
import com.senabo.domain.member.entity.Expense;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findByMemberId(Member memberId);

    List<Expense> deleteByMemberId(Member memberId);
}
