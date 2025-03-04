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

    /*private final IncomeRepository incomeRepository;
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
        existingIncome.setName(incomeRequestDto.getName());
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
        Optional<Income> income = incomeRepository.findById(id);
        if (income.isEmpty()) {
            throw new EntityNotFoundException("Income not found with id: " + id);
        }
        incomeRepository.deleteById(id);
    }

    public List<IncomeResponseDto> getIncomesByCategoryNameAndDateRange(String categoryName, LocalDate startDate, LocalDate endDate) {

        // Əgər belə bir categoryName bazada yoxdursa, istisna at
        if (!incomeCategoryRepository.existsByIncomeCategoryName(categoryName)) {
            throw new RuntimeException("Category not found!");
        }

        // Kateqoriya varsa, indi gəlirləri axtaraq
        List<Income> incomes = incomeRepository.findByIncomeCategory_IncomeCategoryNameAndDateBetween(categoryName, startDate, endDate);

        // Əgər bu kateqoriyaya aid gəlirlər yoxdursa, başqa bir istisna atırıq
        if (incomes.isEmpty()) {
            throw new RuntimeException("Resource not found!");
        }

        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }

    public List<IncomeResponseDto> getIncomesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = incomeRepository.findByDateBetween(startDate, endDate);
        if (incomes.isEmpty()) {
            throw new RuntimeException("Resource not found !");
        }

        return incomes.stream()
                .map(incomeMapper::toIncomeResponseDto)
                .collect(Collectors.toList());
    }*/

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

    // Gəliri yeniləmək
    public IncomeResponseDto updateIncome(Long id, IncomeRequestDto incomeRequestDto) {
        String username = getCurrentUsername();

        // Mevcut geliri ID'ye göre al
        Income existingIncome = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        // Gelir kategorisini al
        IncomeCategory incomeCategory = incomeCategoryRepository.findByIncomeCategoryName(incomeRequestDto.getIncomeCategoryName())
                .orElseThrow(() -> new RuntimeException("Income Category not found with name: " + incomeRequestDto.getIncomeCategoryName()));

        // Kullanıcı doğrulaması yap
        if (!existingIncome.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to update this income.");
        }

        // Geliri güncelle
        existingIncome.setName(incomeRequestDto.getName());
        existingIncome.setAmount(incomeRequestDto.getAmount());
        existingIncome.setDate(incomeRequestDto.getDate());
        existingIncome.setIncomeCategory(incomeCategory);

        // Geliri kaydet
        Income updatedIncome = incomeRepository.save(existingIncome);

        // Güncellenmiş geliri dönüş DTO'suna çevir
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
