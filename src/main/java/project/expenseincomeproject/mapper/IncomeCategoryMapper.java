package project.expenseincomeproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.expenseincomeproject.dto.IncomeCategoryRequestDto;
import project.expenseincomeproject.dto.IncomeCategoryResponseDto;
import project.expenseincomeproject.dto.IncomeRequestDto;
import project.expenseincomeproject.dto.IncomeResponseDto;
import project.expenseincomeproject.exception.UserNotFoundException;
import project.expenseincomeproject.model.Income;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.IncomeCategoryRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncomeCategoryMapper {

    private final UserRepository userRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;


    public IncomeCategory toIncomeCategory(IncomeCategoryRequestDto requestDto) {
        Optional<User> userOptional = userRepository.findByUsername(requestDto.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + requestDto.getUsername());
        }

        IncomeCategory incomeCategory = new IncomeCategory();
        incomeCategory.setIncomeCategoryName(requestDto.getCategoryName());
        incomeCategory.setUser(userOptional.get());

        return incomeCategoryRepository.save(incomeCategory); // Repositor vasitəsi ilə DB-ə yazırıq
    }


    public IncomeCategoryResponseDto toIncomeCategoryResponseDto(IncomeCategory incomeCategory) {
        if (incomeCategory.getUser() == null) {
            throw new UserNotFoundException("User not found for this income category");
        }

        IncomeCategoryResponseDto responseDto = new IncomeCategoryResponseDto();
        responseDto.setCategoryName(incomeCategory.getIncomeCategoryName());
        responseDto.setUsername(incomeCategory.getUser().getUsername());

        return responseDto;
    }

}
