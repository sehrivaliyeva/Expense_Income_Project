package project.expenseincomeproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.dto.IncomeRequestDto;
import project.expenseincomeproject.dto.IncomeResponseDto;
import project.expenseincomeproject.mapper.IncomeMapper;
import project.expenseincomeproject.model.Income;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.IncomeCategoryRepository;
import project.expenseincomeproject.repository.IncomeRepository;
import project.expenseincomeproject.repository.UserRepository;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    private final UserRepository userRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;



    // Gəlir əlavə etmək
    public IncomeResponseDto addIncome(IncomeRequestDto incomeRequestDto) {
        Income income = incomeMapper.toIncome(incomeRequestDto);
        income.setUser(userRepository.findByUsername(incomeRequestDto.getUserName()).orElseThrow(() -> new RuntimeException("User not found")));
        income.setIncomeCategory(incomeCategoryRepository.findByIncomeCategoryName(incomeRequestDto.getIncomeCategoryName()).orElseThrow(() -> new RuntimeException("Category not found")));
        Income savedIncome = incomeRepository.save(income);
        return incomeMapper.toIncomeResponseDto(savedIncome);
    }

    // Gəliri yeniləmək
    public IncomeResponseDto updateIncome(Long id, IncomeRequestDto incomeRequestDto) {
        // Mövcud gəliri ID-yə görə tapmaq
        Income existingIncome = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        // IncomeRequestDto-dan məlumatları alıb mövcud gəliri yeniləmək
        existingIncome.setAmount(incomeRequestDto.getAmount());
        existingIncome.setDate(incomeRequestDto.getDate());

        // User və IncomeCategory tapılıb Income obyektinə təyin edilir
        User user = userRepository.findByUsername(incomeRequestDto.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + incomeRequestDto.getUserName()));
        IncomeCategory incomeCategory = incomeCategoryRepository.findByIncomeCategoryName(incomeRequestDto.getIncomeCategoryName())
                .orElseThrow(() -> new RuntimeException("Income Category not found with name: " + incomeRequestDto.getIncomeCategoryName()));

        existingIncome.setUser(user);
        existingIncome.setIncomeCategory(incomeCategory);

        // Yenilənmiş Income obyektini bazada saxlayırıq
        Income updatedIncome = incomeRepository.save(existingIncome);

        // IncomeResponseDto qaytarmaq
        return incomeMapper.toIncomeResponseDto(updatedIncome);
    }



    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    public List<IncomeResponseDto> getIncomesByCategoryAndDateRange(Long categoryId, LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = incomeRepository.findByIncomeCategoryIdAndDateBetween(categoryId, startDate, endDate);
        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }


    public List<IncomeResponseDto> getIncomesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = incomeRepository.findByDateBetween(startDate, endDate);
        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }
}
