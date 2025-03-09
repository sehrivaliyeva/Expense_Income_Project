package project.expenseincomeproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {


    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    private final UserRepository userRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // Gəlir əlavə etmək
    public IncomeResponseDto addIncome(IncomeRequestDto incomeRequestDto) {
        String username = getCurrentUsername();

        // Eğer giriş yapan kullanıcı adı request'teki kullanıcı adı ile uyuşmazsa, yetki hatası ver
        if (!username.equals(incomeRequestDto.getUserName())) {
            throw new RuntimeException("You are not authorized");
        }
        // Kullanıcıyı veritabanından bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kullanıcının aynı isme sahip bir geliri olup olmadığını kontrol et
        boolean exists = incomeRepository.existsByUserAndName(user, incomeRequestDto.getName());
        if (exists) {
            throw new RuntimeException("An income with the same name already exists.");
        }

        // Gelir kategorisinin geçerli olup olmadığını kontrol et
        IncomeCategory incomeCategory = incomeCategoryRepository.findByIncomeCategoryName(incomeRequestDto.getIncomeCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Kullanıcının bu kategoriye izinli olup olmadığını kontrol et
        Optional<IncomeCategory> userCategory = incomeCategoryRepository.findByIncomeCategoryNameAndUser(incomeRequestDto.getIncomeCategoryName(), user);
        if (userCategory.isEmpty()) {
            throw new RuntimeException("You do not have permission to use this category.");
        }

        // Geliri oluştur
        Income income = incomeMapper.toIncome(incomeRequestDto);
        income.setUser(user);  // Kullanıcıyı ata
        income.setIncomeCategory(incomeCategory);  // Kategoriyi ata

        // Geliri kaydet
        Income savedIncome = incomeRepository.save(income);

        // Kaydedilen geliri dönüş DTO'suna çevir
        return incomeMapper.toIncomeResponseDto(savedIncome);
    }
    public IncomeResponseDto updateIncome(Long id,IncomeRequestDto incomeRequestDto) {
        String username = getCurrentUsername();

        // Check if the logged-in user is authorized to update the expense
        if (!username.equals(incomeRequestDto.getUserName())) {
            throw new RuntimeException("You are not authorized to update income for this user.");
        }

        // Retrieve the existing expense by its ID
        Income existingIncome = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        // Check if the logged-in user is the owner of the existing expense
        if (!existingIncome.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to update this income.");
        }

        // Fetch the ExpenseCategory from the repository
       IncomeCategory incomeCategory = incomeCategoryRepository.findByIncomeCategoryName(incomeRequestDto.getIncomeCategoryName())
                .orElseThrow(() -> new RuntimeException("Income Category not found with name: " + incomeRequestDto.getIncomeCategoryName()));

        // Ensure that the category belongs to the user
        if (!incomeCategory.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You have not this category.");
        }

        // Update the expense details
        existingIncome.setAmount(incomeRequestDto.getAmount());
        existingIncome.setName(incomeRequestDto.getName());
        existingIncome.setDate(incomeRequestDto.getDate());
        existingIncome.setIncomeCategory(incomeCategory);

        // Save the updated expense
        Income updatedIncome = incomeRepository.save(existingIncome);

        // Return the updated ExpenseResponseDto
        return incomeMapper.toIncomeResponseDto(updatedIncome);
    }

    // Geliri silmek
    public void deleteIncome(Long id) {
        String username = getCurrentUsername();
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Income not found with id: " + id));

        if (!income.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this income.");
        }

        incomeRepository.deleteById(id);
    }

    // Kullanıcının gelirlerini almak
    public List<IncomeResponseDto> getUserIncomes() {
        String username = getCurrentUsername();
        List<Income> incomes = incomeRepository.findByUser_Username(username);

        if (incomes.isEmpty()) {
            throw new RuntimeException("No incomes found!");
        }

        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }

    // Tarih aralığına göre gelirleri almak
    public List<IncomeResponseDto> getIncomesByDateRange(LocalDate startDate, LocalDate endDate) {
        String username = getCurrentUsername();
        List<Income> incomes = incomeRepository.findByUser_UsernameAndDateBetween(username, startDate, endDate);

        if (incomes.isEmpty()) {
            throw new RuntimeException("No incomes found!");
        }

        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }

    // Gelirleri kategori adı ve tarih aralığına göre almak
    public List<IncomeResponseDto> getIncomesByCategoryNameAndDateRange(String categoryName, LocalDate startDate, LocalDate endDate) {
        String username = getCurrentUsername();

        // Eğer kategorinin adı geçerli değilse hata fırlat
        if (!incomeCategoryRepository.existsByIncomeCategoryName(categoryName)) {
            throw new RuntimeException("Category not found!");
        }

        List<Income> incomes = incomeRepository.findByUser_UsernameAndIncomeCategory_IncomeCategoryNameAndDateBetween(
                username, categoryName, startDate, endDate);

        if (incomes.isEmpty()) {
            throw new RuntimeException("No incomes found!");
        }

        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }
}
