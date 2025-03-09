package project.expenseincomeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.expenseincomeproject.dto.IncomeRequestDto;
import project.expenseincomeproject.dto.IncomeResponseDto;
import project.expenseincomeproject.service.IncomeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;


    @PostMapping("/add")
    public ResponseEntity<IncomeResponseDto> addIncome(@RequestBody IncomeRequestDto incomeRequestDto) {
        IncomeResponseDto incomeResponseDto = incomeService.addIncome(incomeRequestDto);
        return ResponseEntity.ok(incomeResponseDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<IncomeResponseDto> updateIncome(@PathVariable Long id, @RequestBody IncomeRequestDto incomeRequestDto) {
        IncomeResponseDto incomeResponseDto = incomeService.updateIncome(id, incomeRequestDto);
        return ResponseEntity.ok(incomeResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/category-and-date-range/{categoryName}")
    public ResponseEntity<List<IncomeResponseDto>> getIncomesByCategoryAndDateRange(
            @PathVariable String categoryName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<IncomeResponseDto> incomes = incomeService. getIncomesByCategoryNameAndDateRange(categoryName, startDate, endDate);
        return ResponseEntity.ok(incomes);
    }

    // Yalnız tarix aralığı ilə gəlirləri tapmaq
    @GetMapping("/date-range")
    public ResponseEntity<List<IncomeResponseDto>> getIncomesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<IncomeResponseDto> incomes = incomeService.getIncomesByDateRange(startDate, endDate);
        return ResponseEntity.ok(incomes);
    }
}
