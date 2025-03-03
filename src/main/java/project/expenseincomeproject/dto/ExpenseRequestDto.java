package project.expenseincomeproject.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ExpenseRequestDto {
    private String name;
    private Double amount;
    private LocalDate date;
    private String expenseCategoryName;
    private String userName;
}
