package project.expenseincomeproject.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.expenseincomeproject.dto.IncomeRequestDto;
import project.expenseincomeproject.dto.IncomeResponseDto;
import project.expenseincomeproject.model.Income;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.IncomeCategoryRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class IncomeMapper {

    private final UserRepository userRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;


    public Income toIncome(IncomeRequestDto incomeRequestDto) {
        Income income = new Income();
        income.setName(incomeRequestDto.getName());
        income.setAmount(incomeRequestDto.getAmount());
        income.setDate(incomeRequestDto.getDate());

        Optional<User> user = userRepository.findByUsername(incomeRequestDto.getUserName());
        if (user.isPresent()) {
            income.setUser(user.get());
        } else {
            throw new RuntimeException("User not found with username: " + incomeRequestDto.getUserName());
        }

        Optional<IncomeCategory> incomeCategory = incomeCategoryRepository.findByIncomeCategoryName(incomeRequestDto.getIncomeCategoryName());
        if (incomeCategory.isPresent()) {
            income.setIncomeCategory(incomeCategory.get());
        } else {
            throw new RuntimeException("Income Category not found with name: " + incomeRequestDto.getIncomeCategoryName());
        }

        return income;
    }

    public IncomeResponseDto toIncomeResponseDto(Income income) {
        IncomeResponseDto dto = new IncomeResponseDto();
        dto.setName(income.getName());
        dto.setAmount(income.getAmount());
        dto.setDate(income.getDate());
        dto.setUsername(income.getUser().getUsername());  // Username
        dto.setIncomeCategoryName(income.getIncomeCategory().getIncomeCategoryName());
        return dto;
    }

}
