package project.expenseincomeproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.dto.ExpenseCategoryRequestDto;
import project.expenseincomeproject.dto.ExpenseCategoryResponseDto;
import project.expenseincomeproject.exception.UserNotFoundException;
import project.expenseincomeproject.mapper.ExpenseCategoryMapper;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.ExpenseCategoryRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
   private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    private final ExpenseCategoryMapper expenseCategoryMapper;

    public ExpenseCategoryResponseDto save(ExpenseCategoryRequestDto categoryRequestDto) {
        // Giriş etmiş istifadəçinin authentication məlumatlarını alırıq
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Authentication null ola bilər, ona görə yoxlama edirik
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated. Please log in first.");
        }

        // Cari istifadəçinin username-ni əldə edirik
        String username = authentication.getName();

        // Request DTO-dan gələn username ilə müqayisə edirik
        if (!username.equals(categoryRequestDto.getUsername())) {
            throw new RuntimeException("Usernames do not match. Only the authenticated user can add categories.");
        }

        // Sistemdə olan istifadəçini yoxlayaq
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        User user = userOptional.get();

        // Eyni ExpenseCategory artıq bu istifadəçi tərəfindən əlavə edilibmi yoxlayaq
        Optional<ExpenseCategory> existingCategory = expenseCategoryRepository.findByExpenseCategoryNameAndUser(categoryRequestDto.getCategoryName(), user);
        if (existingCategory.isPresent()) {
            throw new RuntimeException("Expense category with this name already exists for the current user.");
        }

        // Yeni ExpenseCategory yaradırıq və bazaya əlavə edirik
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setExpenseCategoryName(categoryRequestDto.getCategoryName());
        expenseCategory.setUser(user);

        // ExpenseCategory-ni bazaya qeyd edirik
        expenseCategory = expenseCategoryRepository.save(expenseCategory);

        // ExpenseCategory response DTO-ya çeviririk
        return expenseCategoryMapper.toExpenseCategoryResponseDto(expenseCategory);
    }


    // Bütün ExpenseCategory-ləri əldə etmək üçün, yalnız istifadəçinin məlumatları
    public List<ExpenseCategoryResponseDto> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Cari istifadəçinin username-ini alırıq

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        User user = userOptional.get();

        List<ExpenseCategory> expenseCategories = expenseCategoryRepository.findByUser(user);
        return expenseCategories.stream()
                .map(expenseCategory -> new ExpenseCategoryResponseDto(
                        expenseCategory.getExpenseCategoryName(),
                        expenseCategory.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    // ExpenseCategory yeniləmək üçün
    public ExpenseCategoryResponseDto update(Long id, ExpenseCategoryRequestDto categoryRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Hazırkı istifadəçinin username-ni alırıq

        // Request-dən gələn username ilə hazırkı istifadəçi eyni olmalıdır
        if (!currentUsername.equals(categoryRequestDto.getUsername())) {
            throw new UserNotFoundException("Authenticated user does not match the request username");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + currentUsername));

        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpenseCategory not found with id: " + id));

        // ExpenseCategory hazırkı istifadəçiyə məxsus olmalıdır
        if (!expenseCategory.getUser().equals(user)) {
            throw new RuntimeException("ExpenseCategory is not owned by the current user");
        }

        // Yeni ad təyin edilir və yenilənmiş məlumat bazaya yazılır
        expenseCategory.setExpenseCategoryName(categoryRequestDto.getCategoryName());
        expenseCategory.setUser(user);
        expenseCategory = expenseCategoryRepository.save(expenseCategory);

        return expenseCategoryMapper.toExpenseCategoryResponseDto(expenseCategory);
    }

    // ExpenseCategory silmək üçün
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Cari istifadəçinin username-ini alırıq

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        User user = userOptional.get();

        Optional<ExpenseCategory> expenseCategoryOptional = expenseCategoryRepository.findById(id);
        if (expenseCategoryOptional.isEmpty() || !expenseCategoryOptional.get().getUser().equals(user)) {
            throw new RuntimeException("ExpenseCategory not found or not owned by the current user");
        }

        expenseCategoryRepository.deleteById(id);
    }
}
