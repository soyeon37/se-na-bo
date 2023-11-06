package com.senabo.domain.expense.controller;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.disease.dto.response.DiseaseResponse;
import com.senabo.domain.expense.dto.request.CreateExpenseRequest;
import com.senabo.domain.expense.dto.response.ExpenseResponse;
import com.senabo.domain.expense.dto.response.TotalAmountExpenseResponse;
import com.senabo.domain.expense.entity.Expense;
import com.senabo.domain.expense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ExpenseResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "비용 전체 조회", description = "비용 내역을 전체 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpense(@RequestParam String email) {
        List<Expense> expense = expenseService.getExpense(email);
        if (expense.isEmpty()) return ApiResponse.fail("비용 전체 조회 실패", null);
        List<ExpenseResponse> response = expense.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 전체 조회 성공", response);
    }


    @GetMapping("/list/{week}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 status: SUCCESS, 내역이 없으면 status: FAIL", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ExpenseResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "비용 주간 조회", description = "비용 내역을 주간 조회한다.")
    public ApiResponse<List<ExpenseResponse>> getExpenseWeek(@RequestParam String email, @PathVariable  int week) {
        List<Expense> expense = expenseService.getExpenseWeek(email, week);
        if (expense.isEmpty()) return ApiResponse.fail("비용 "+ week + "주차 조회 실패", null);
        List<ExpenseResponse> response = expense.stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("비용 " + week + "주차 조회 성공", response);
    }


    @GetMapping("/total")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 totalAmount, 내역이 없으면 0.0.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = TotalAmountExpenseResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "비용 총 금액 조회", description = "비용 총 금액을 조회한다.")
    public ApiResponse<TotalAmountExpenseResponse> getTotalExpense(@RequestParam String email) {
        TotalAmountExpenseResponse response = expenseService.getExpenseTotal(email);
        return ApiResponse.success("비용 총 금액 조회 성공", response);
    }


    @GetMapping("/total/{week}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내역이 있으면 totalAmount, 내역이 없으면 0.0.", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = TotalAmountExpenseResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "USER NOT FOUND")
    }
    )
    @Operation(summary = "비용 주간 총 금액 조회", description = "비용 주간 총 금액을 조회한다.")
    public ApiResponse<TotalAmountExpenseResponse> getTotalExpenseWeek(@RequestParam String email, @PathVariable int week) {
        TotalAmountExpenseResponse response = expenseService.getExpenseTotalWeek(email, week);
        return ApiResponse.success("비용 " + week + "주차 총 금액 조회 성공", response);
    }


    @DeleteMapping("/remove")
    @Operation(summary = "비용 내역 삭제", description = "비용 내역을 삭제한다.")
    public ApiResponse<Object> removeExpense(@RequestParam String email) {
        expenseService.removeExpense(email);
        return ApiResponse.success("비용 삭제 성공", true);
    }

}
