package com.senabo.domain.expense.container;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.expense.dto.request.CreateExpenseRequest;
import com.senabo.domain.expense.dto.response.ExpenseResponse;
import com.senabo.domain.expense.entity.Expense;
import com.senabo.domain.expense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expense")
@Tag(name = "Expense", description = "Expense API Document")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/save")
    @Operation(summary = "비용 저장", description = "비용을 저장한다.")
    public ApiResponse<ExpenseResponse> createExpense(@RequestParam String email, @RequestBody CreateExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(email, request);
        return ApiResponse.success("비용 저장 성공", response);
    }


    @GetMapping("/list")
    @Operation(summary = "비용 전체 조회", description = "비용 내역을 전체 조회한다.")
    public ApiResponse<Map<String ,Object>> getExpense(@RequestParam String email) {
        List<Expense> expense = expenseService.getExpense(email);
        if (expense.isEmpty()) return ApiResponse.fail("비용 전체 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("expenseList", expense.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("비용 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @Operation(summary = "비용 주간 조회", description = "비용 내역을 주간 조회한다.")
    public ApiResponse<Map<String ,Object>> getExpenseWeek(@RequestParam String email, @RequestParam int week) {
        List<Expense> expense = expenseService.getExpenseWeek(email, week);
        if (expense.isEmpty()) return ApiResponse.fail("비용 "+ week + "주차 조회 실패", null);
        Map<String, Object> response = new HashMap<>();
        response.put("expenseList", expense.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList()));
        return ApiResponse.success("비용 " + week + "주차 조회 성공", response);
    }


    @GetMapping("/total")
    @Operation(summary = "비용 총 금액 조회", description = "비용 총 금액을 조회한다.")
    public ApiResponse<Map<String ,Object>> getTotalExpense(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        Double totalAmount = expenseService.getExpenseTotal(email);
        response.put("totalAmount", totalAmount);
        return ApiResponse.success("비용 총 금액 조회 성공", response);
    }


    @GetMapping("/total/{week}")
    @Operation(summary = "비용 주간 총 금액 조회", description = "비용 주간 총 금액을 조회한다.")
    public ApiResponse<Map<String, Object>> getTotalExpenseWeek(@RequestParam String email, @RequestParam int week) {
        Map<String, Object> response = new HashMap<>();
        Double totalAmount = expenseService.getExpenseTotalWeek(email, week);
        response.put("totalAmount", totalAmount);
        return ApiResponse.success("비용 " + week + "주차 총 금액 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "비용 내역 삭제", description = "비용 내역을 삭제한다.")
    public ApiResponse<Object> removeExpense(@RequestParam String email) {
        expenseService.removeExpense(email);
        return ApiResponse.success("비용 삭제 성공", true);
    }

}
