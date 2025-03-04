package project.expenseincomeproject.repository;


import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByExpenseCategoryName(String expenseCategoryName);

    boolean existsByExpenseCategoryName(String expenseCategoryName);

    Optional<ExpenseCategory> findByExpenseCategoryNameAndUser(@NotBlank(message = "Category name cannot be empty") String categoryName, User user);

    List<ExpenseCategory> findByUser(User user);
}
