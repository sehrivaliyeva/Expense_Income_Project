package project.expenseincomeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.expenseincomeproject.model.IncomeCategory;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeCategoryResponseDto {
    private String categoryName;
    private String username;


}
