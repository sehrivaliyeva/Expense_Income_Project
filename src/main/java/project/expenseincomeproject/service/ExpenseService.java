package project.expenseincomeproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.dto.ExpenseRequestDto;
import project.expenseincomeproject.dto.ExpenseResponseDto;
import project.expenseincomeproject.mapper.ExpenseMapper;
import project.expenseincomeproject.model.Expense;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.ExpenseCategoryRepository;
import project.expenseincomeproject.repository.ExpenseRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseResponseDto addExpense(ExpenseRequestDto expenseRequestDto) {
        Expense expense = expenseMapper.toExpense(expenseRequestDto);
        expense.setUser(userRepository.findByUsername(expenseRequestDto.getUserName()).orElseThrow(() -> new RuntimeException("User not found")));
        expense.setExpenseCategory(expenseCategoryRepository.findByExpenseCategoryName(expenseRequestDto.getExpenseCategoryName()).orElseThrow(() -> new RuntimeException("Category not found")));
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponseDto(savedExpense);
    }

    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto expenseRequestDto) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        existingExpense.setAmount(expenseRequestDto.getAmount());
        existingExpense.setDate(expenseRequestDto.getDate());

        User user = userRepository.findByUsername(expenseRequestDto.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + expenseRequestDto.getUserName()));
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByExpenseCategoryName(expenseRequestDto.getExpenseCategoryName())
                .orElseThrow(() -> new RuntimeException("Expense Category not found with name: " + expenseRequestDto.getExpenseCategoryName()));

        existingExpense.setUser(user);
        existingExpense.setExpenseCategory(expenseCategory);

        Expense updatedExpense = expenseRepository.save(existingExpense);
        return expenseMapper.toExpenseResponseDto(updatedExpense);
    }

    public void deleteExpense(Long id) {
        // İstifadəçinin ID-yə əsasən məlumatı tapın
        Optional<Expense> expense = expenseRepository.findById(id);

        // Əgər məlumat tapılmasa, istisna atın
        if (expense.isEmpty()) {
            throw new EntityNotFoundException("Expense not found with id: " + id);
        }

        // Əgər məlumat tapılarsa, silin
        expenseRepository.deleteById(id);
    }

    public List<ExpenseResponseDto> getExpensesByCategoryNameAndDateRange(String categoryName, LocalDate startDate, LocalDate endDate) {

        // Əgər belə bir categoryName bazada yoxdursa, istisna at
        if (!expenseCategoryRepository.existsByExpenseCategoryName(categoryName)) {
            throw new RuntimeException("Category not found!");
        }

        // Kateqoriya varsa, indi xərcləri axtaraq
        List<Expense> expenses = expenseRepository.findByExpenseCategory_ExpenseCategoryNameAndDateBetween(categoryName, startDate, endDate);

        // Əgər bu kateqoriyaya aid xərclər yoxdursa, başqa bir istisna atırıq
        if (expenses.isEmpty()) {
            throw new RuntimeException("Resource not found !");
        }

        return expenses.stream()
                .map(expenseMapper::toExpenseResponseDto)
                .collect(Collectors.toList());
    }



    public List<ExpenseResponseDto> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByDateBetween(startDate, endDate);

        if (expenses.isEmpty()) {
            throw new RuntimeException("Resource not found !");
        }

        return expenses.stream()
                .map(expenseMapper::toExpenseResponseDto)
                .collect(Collectors.toList());
    }

}
