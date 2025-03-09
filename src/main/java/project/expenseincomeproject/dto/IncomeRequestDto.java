package project.expenseincomeproject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeRequestDto {

    private String name;
    private Double amount;
    private LocalDate date;

    private String incomeCategoryName;
    private String userName;

}
