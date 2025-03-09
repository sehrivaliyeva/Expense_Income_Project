package project.expenseincomeproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.dto.IncomeCategoryRequestDto;
import project.expenseincomeproject.dto.IncomeCategoryResponseDto;
import project.expenseincomeproject.exception.UserNotFoundException;
import project.expenseincomeproject.mapper.IncomeCategoryMapper;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.IncomeCategoryRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;
    private final IncomeCategoryMapper incomeCategoryMapper;

    // IncomeCategory əlavə etmək üçün
    public IncomeCategoryResponseDto save(IncomeCategoryRequestDto categoryRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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

        // Eyni IncomeCategory artıq bu istifadəçi tərəfindən əlavə edilibmi yoxlayaq
        Optional<IncomeCategory> existingCategory = incomeCategoryRepository.findByIncomeCategoryNameAndUser(categoryRequestDto.getCategoryName(), user);
        if (existingCategory.isPresent()) {
            throw new RuntimeException("Income category with this name already exists for the current user.");
        }

        // Yeni IncomeCategory yaradırıq və bazaya əlavə edirik
        IncomeCategory incomeCategory = new IncomeCategory();
        incomeCategory.setIncomeCategoryName(categoryRequestDto.getCategoryName());
        incomeCategory.setUser(user);

        // IncomeCategory-ni bazaya qeyd edirik
        incomeCategory = incomeCategoryRepository.save(incomeCategory);

        // IncomeCategory response DTO-ya çeviririk
        return incomeCategoryMapper.toIncomeCategoryResponseDto(incomeCategory);
    }



    // Bütün IncomeCategory-ləri əldə etmək üçün, yalnız istifadəçinin məlumatları
    public List<IncomeCategoryResponseDto> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Cari istifadəçinin username-ini alırıq

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        User user = userOptional.get();

        List<IncomeCategory> incomeCategories = incomeCategoryRepository.findByUser(user);
        return incomeCategories.stream()
                .map(incomeCategory -> new IncomeCategoryResponseDto(
                        incomeCategory.getIncomeCategoryName(),
                        incomeCategory.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    // IncomeCategory yeniləmək üçün
    public IncomeCategoryResponseDto update(Long id, IncomeCategoryRequestDto categoryRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Hazırkı istifadəçinin username-ni alırıq

        // Request-dən gələn username ilə hazırkı istifadəçi eyni olmalıdır
        if (!currentUsername.equals(categoryRequestDto.getUsername())) {
            throw new UserNotFoundException("Authenticated user does not match the request username");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + currentUsername));


        IncomeCategory incomeCategory = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncomeCategory not found with id: " + id));

        // IncomeCategory hazırkı istifadəçiyə məxsus olmalıdır
        if (!incomeCategory.getUser().equals(user)) {
            throw new RuntimeException("IncomeCategory is not owned by the current user");
        }


        // Yeni ad təyin edilir və yenilənmiş məlumat bazaya yazılır
        incomeCategory.setIncomeCategoryName(categoryRequestDto.getCategoryName());
        incomeCategory.setUser(user);
        incomeCategory = incomeCategoryRepository.save(incomeCategory);

        return incomeCategoryMapper.toIncomeCategoryResponseDto(incomeCategory);
    }



    // IncomeCategory silmək üçün
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Cari istifadəçinin username-ini alırıq

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        User user = userOptional.get();

        Optional<IncomeCategory> incomeCategoryOptional = incomeCategoryRepository.findById(id);
        if (incomeCategoryOptional.isEmpty() || !incomeCategoryOptional.get().getUser().equals(user)) {
            throw new RuntimeException("IncomeCategory not found or not owned by the current user");
        }

        incomeCategoryRepository.deleteById(id);
    }
}
