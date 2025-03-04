package project.expenseincomeproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.expenseincomeproject.dto.ExpenseRequestDto;
import project.expenseincomeproject.dto.ExpenseResponseDto;
import project.expenseincomeproject.model.Expense;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.ExpenseCategoryRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExpenseMapper {

    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;


    public Expense toExpense(ExpenseRequestDto expenseRequestDto) {
        Expense expense = new Expense();
        expense.setName(expenseRequestDto.getName());
        expense.setAmount(expenseRequestDto.getAmount());
        expense.setDate(expenseRequestDto.getDate());

        Optional<User> user = userRepository.findByUsername(expenseRequestDto.getUserName());
        if (user.isPresent()) {
            expense.setUser(user.get());
        } else {
            throw new RuntimeException("User not found with username: " + expenseRequestDto.getUserName());
        }

        Optional<ExpenseCategory> expenseCategory = expenseCategoryRepository.findByExpenseCategoryName(expenseRequestDto.getExpenseCategoryName());
        if (expenseCategory.isPresent()) {
            expense.setExpenseCategory(expenseCategory.get());
        } else {
            throw new RuntimeException("Expense Category not found with name: " + expenseRequestDto.getExpenseCategoryName());
        }

        return expense;
    }

    public ExpenseResponseDto toExpenseResponseDto(Expense expense) {
        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setName(expense.getName());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setUserName(expense.getUser().getUsername());  // Username
        dto.setExpenseCategoryName(expense.getExpenseCategory().getExpenseCategoryName());
        return dto;
    }

}
