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
    @PostMapping("/expense")
    @Operation(summary = "비용 생성", description = "비용을 저장한다.")
    public ApiResponse<ExpenseResponse> createExpense(@RequestParam(name = "id") Long id, @RequestBody CreateExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(id, request);
        return ApiResponse.success("비용 저장 완료", response);
    }


    @GetMapping("/expense")
    @Operation(summary = "비용 전체 조회", description = "비용 내역을 전체 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpense(@RequestParam(name = "id") Long id) {
        List<Expense> response = expenseService.getExpense(id);
        List<ExpenseResponse> responseList = response.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 전체 조회", responseList);
    }


    @GetMapping("/expense/{week}")
    @Operation(summary = "비용 주간 조회", description = "비용 내역을 주간 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpense(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        List<Expense> response = expenseService.getExpenseWeek(id, week);
        List<ExpenseResponse> responseList = response.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 주간 조회", responseList);
    }


    @GetMapping("/expense/total")
    @Operation(summary = "비용 총 금액 조회", description = "비용 총 금액을 조회한다.")
    public ApiResponse<Map<String, Object>> getTotalExpense(@RequestParam(name = "id") Long id) {
        Map<String, Object> map = new HashMap<>();
        Long totalAmount = expenseService.getExpenseTotal(id);
        map.put("totalAmount", totalAmount);
        return ApiResponse.success("비용 총 금액 조회", map);
    }


    @GetMapping("/expense/total/{week}")
    @Operation(summary = "비용 주간 총 금액 조회", description = "비용 주간 총 금액을 조회한다.")
    public ApiResponse<Map<String, Object>> getTotalExpenseWeek(@RequestParam(name = "id") Long id, @RequestParam(name = "week") int week) {
        Map<String, Object> map = new HashMap<>();
        Long totalAmount = expenseService.getExpenseTotalWeek(id, week);
        map.put("totalAmount", totalAmount);
        return ApiResponse.success("비용 주간 총 금액 조회", map);
    }


    @DeleteMapping("/expense")
    @Operation(summary = "비용 내역 삭제", description = "비용 내역을 삭제한다.")
    public ApiResponse<Object> removeExpense(@RequestParam(name = "id") Long id) {
        expenseService.removeExpense(id);
        return ApiResponse.success("비용 삭제", true);
    }

}
