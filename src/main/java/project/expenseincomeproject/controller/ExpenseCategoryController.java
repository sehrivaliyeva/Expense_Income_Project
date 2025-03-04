package project.expenseincomeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.expenseincomeproject.dto.ExpenseCategoryRequestDto;
import project.expenseincomeproject.dto.ExpenseCategoryResponseDto;
import project.expenseincomeproject.service.ExpenseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/v1/expense-categories")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    // Create a new ExpenseCategory
    @PostMapping("/add")
    public ResponseEntity<ExpenseCategoryResponseDto> createCategory(@RequestBody ExpenseCategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(expenseCategoryService.save(categoryRequestDto));
    }

    // Get all ExpenseCategories
    @GetMapping
    public ResponseEntity<List<ExpenseCategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(expenseCategoryService.getAll());
    }

    // Update an existing ExpenseCategory
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody ExpenseCategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(expenseCategoryService.update(id, categoryRequestDto));
    }

    // Delete an ExpenseCategory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        expenseCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
