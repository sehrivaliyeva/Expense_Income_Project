package project.expenseincomeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.expenseincomeproject.dto.ExpenseRequestDto;
import project.expenseincomeproject.dto.ExpenseResponseDto;
import project.expenseincomeproject.service.ExpenseService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<ExpenseResponseDto> addExpense(@RequestBody ExpenseRequestDto expenseRequestDto) {
        ExpenseResponseDto expenseResponseDto = expenseService.addExpense(expenseRequestDto);
        return ResponseEntity.ok(expenseResponseDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDto expenseRequestDto) {
        ExpenseResponseDto expenseResponseDto = expenseService.updateExpense(id, expenseRequestDto);
        return ResponseEntity.ok(expenseResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category-and-date-range/{categoryName}")
    public ResponseEntity<List<ExpenseResponseDto>> getExpensesByCategoryAndDateRange(
            @PathVariable String categoryName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ExpenseResponseDto> expenses = expenseService.getExpensesByCategoryNameAndDateRange(categoryName, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponseDto>> getExpensesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ExpenseResponseDto> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
}
