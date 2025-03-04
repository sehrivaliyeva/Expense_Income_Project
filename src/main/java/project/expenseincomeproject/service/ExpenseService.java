package project.expenseincomeproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


    public ExpenseResponseDto addExpense(ExpenseRequestDto expenseRequestDto) {
        // Geçerli kullanıcı adını al
        String username = getCurrentUsername();

        // Eğer giriş yapan kullanıcı adı request'teki kullanıcı adı ile uyuşmazsa, yetki hatası ver
        if (!username.equals(expenseRequestDto.getUserName())) {
            throw new RuntimeException("You are not authorized");
        }

        // Kullanıcıyı veritabanından bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kullanıcının aynı isme sahip bir harcaması olup olmadığını kontrol et
        boolean exists = expenseRepository.existsByUserAndName(user, expenseRequestDto.getName());
        if (exists) {
            throw new RuntimeException("An expense with the same name already exists.");
        }

        // Harcama kategorisinin geçerli olup olmadığını kontrol et
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByExpenseCategoryName(expenseRequestDto.getExpenseCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Kullanıcının bu kategoriye izinli olup olmadığını kontrol et
        // Kategoriye sahip olup olmadığını kontrol et
        Optional<ExpenseCategory> userCategory = expenseCategoryRepository.findByExpenseCategoryNameAndUser(expenseRequestDto.getExpenseCategoryName(), user);
        if (userCategory.isEmpty()) {
            throw new RuntimeException("You have not this category.");
        }

        // Harcamayı oluştur
        Expense expense = expenseMapper.toExpense(expenseRequestDto);
        expense.setUser(user);  // Kullanıcıyı ata
        expense.setExpenseCategory(expenseCategory);  // Kategoriyi ata

        // Harcamayı kaydet
        Expense savedExpense = expenseRepository.save(expense);

        // Kaydedilen harcamayı dönüş DTO'suna çevir
        return expenseMapper.toExpenseResponseDto(savedExpense);
    }



    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto expenseRequestDto) {
        String username = getCurrentUsername();

        // Check if the logged-in user is authorized to update the expense
        if (!username.equals(expenseRequestDto.getUserName())) {
            throw new RuntimeException("You are not authorized to update expense for this user.");
        }

        // Retrieve the existing expense by its ID
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        // Check if the logged-in user is the owner of the existing expense
        if (!existingExpense.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to update this expense.");
        }

        // Fetch the ExpenseCategory from the repository
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByExpenseCategoryName(expenseRequestDto.getExpenseCategoryName())
                .orElseThrow(() -> new RuntimeException("Expense Category not found with name: " + expenseRequestDto.getExpenseCategoryName()));

        // Ensure that the category belongs to the user
        if (!expenseCategory.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You have not this category.");
        }

        // Update the expense details
        existingExpense.setAmount(expenseRequestDto.getAmount());
        existingExpense.setName(expenseRequestDto.getName());
        existingExpense.setDate(expenseRequestDto.getDate());
        existingExpense.setExpenseCategory(expenseCategory);

        // Save the updated expense
        Expense updatedExpense = expenseRepository.save(existingExpense);

        // Return the updated ExpenseResponseDto
        return expenseMapper.toExpenseResponseDto(updatedExpense);
    }


    public void deleteExpense(Long id) {
        String username = getCurrentUsername();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id: " + id));

        if (!expense.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this expense.");
        }

        expenseRepository.deleteById(id);
    }

    public List<ExpenseResponseDto> getUserExpenses() {
        String username = getCurrentUsername();
        List<Expense> expenses = expenseRepository.findByUser_Username(username);

        if (expenses.isEmpty()) {
            throw new RuntimeException("No expenses found!");
        }

        return expenses.stream()
                .map(expenseMapper::toExpenseResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponseDto> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        String username = getCurrentUsername();
        List<Expense> expenses = expenseRepository.findByUser_UsernameAndDateBetween(username, startDate, endDate);

        if (expenses.isEmpty()) {
            throw new RuntimeException("No expenses found!");
        }

        return expenses.stream()
                .map(expenseMapper::toExpenseResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponseDto> getExpensesByCategoryNameAndDateRange(String categoryName, LocalDate startDate, LocalDate endDate) {
        String username = getCurrentUsername();

        if (!expenseCategoryRepository.existsByExpenseCategoryName(categoryName)) {
            throw new RuntimeException("Category not found!");
        }

        List<Expense> expenses = expenseRepository.findByUser_UsernameAndExpenseCategory_ExpenseCategoryNameAndDateBetween(
                username, categoryName, startDate, endDate);

        if (expenses.isEmpty()) {
            throw new RuntimeException("No expenses found!");
        }

        return expenses.stream()
                .map(expenseMapper::toExpenseResponseDto)
                .collect(Collectors.toList());
    }

}
