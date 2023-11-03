package com.senabo.domain.expense.service;

import com.senabo.domain.expense.dto.response.ExpenseResponse;
import com.senabo.domain.expense.repository.ExpenseRepository;
import com.senabo.domain.expense.dto.request.CreateExpenseRequest;
import com.senabo.domain.expense.entity.Expense;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public ExpenseResponse createExpense(Long id, CreateExpenseRequest request) {
        Member member = findById(id);
        Expense expense = expenseRepository.save(
                new Expense(member, request.item(), request.detail(), request.amount()));
        try {
            expenseRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return ExpenseResponse.from(expense);
    }

    @Transactional
    public List<Expense> getExpense(Long id) {
        Member member = findById(id);
        List<Expense> expenseList = expenseRepository.findByMemberId(member);
        if (expenseList.isEmpty()) {
            throw new EntityNotFoundException("Expense에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return expenseList;
    }
    @Transactional
    public Long getExpenseTotal(Long id) {
        Member member = findById(id);
        Long totalAmount = expenseRepository.getTotalAmount(member);
        return totalAmount;
    }

    @Transactional
    public Long getExpenseTotalWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        Long totalAmount = expenseRepository.getTotalAmountWeek(member, endTime, startTime);
        return totalAmount;
    }


    @Transactional
    public List<Expense> getExpenseWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Expense> expenseList = expenseRepository.findExpenseWeek(member, endTime, startTime);
        if (expenseList.isEmpty()) {
            throw new EntityNotFoundException("Expense에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return expenseList;
    }

    @Transactional
    public void removeExpense(Long id) {
        try {
            Member member = findById(id);
            List<Expense> list = expenseRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }


    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
