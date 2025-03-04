package project.expenseincomeproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseCategoryRequestDto {
    @NotBlank(message = "Category name cannot be empty")
    private String categoryName;

    @NotBlank(message = "Username cannot be empty")
    private String username;
}
