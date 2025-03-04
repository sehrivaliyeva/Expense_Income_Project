package project.expenseincomeproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.expenseincomeproject.dto.ExpenseCategoryRequestDto;
import project.expenseincomeproject.dto.ExpenseCategoryResponseDto;
import project.expenseincomeproject.exception.UserNotFoundException;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.ExpenseCategoryRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExpenseCategoryMapper {

    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    // ExpenseCategoryRequestDto məlumatını ExpenseCategory modelinə çevirir
    public ExpenseCategory toExpenseCategory(ExpenseCategoryRequestDto requestDto) {
        // İstifadəçi bazada varmı yoxlanır
        Optional<User> userOptional = userRepository.findByUsername(requestDto.getUsername());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with username: " + requestDto.getUsername());
        }

        // ExpenseCategory yaratmaq
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setExpenseCategoryName(requestDto.getCategoryName());
        expenseCategory.setUser(userOptional.get());

        // ExpenseCategory-ni DB-yə əlavə edir
        return expenseCategoryRepository.save(expenseCategory);
    }

    // ExpenseCategory modelini ExpenseCategoryResponseDto-ya çevirir
    public ExpenseCategoryResponseDto toExpenseCategoryResponseDto(ExpenseCategory expenseCategory) {
        // User yoxlanır
        if (expenseCategory.getUser() == null) {
            throw new UserNotFoundException("User not found for this expense category");
        }

        // ResponseDto yaratmaq
        ExpenseCategoryResponseDto responseDto = new ExpenseCategoryResponseDto();
        responseDto.setCategoryName(expenseCategory.getExpenseCategoryName());
        responseDto.setUsername(expenseCategory.getUser().getUsername());

        return responseDto;
    }

}
