package project.expenseincomeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.Expense;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
   // List<Expense> findByExpenseCategoryIdAndDateBetween(Long categoryId, LocalDate startDate, LocalDate endDate);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expense> findByExpenseCategory_ExpenseCategoryNameAndDateBetween(
            String expenseCategoryName, LocalDate startDate, LocalDate endDate);
}
