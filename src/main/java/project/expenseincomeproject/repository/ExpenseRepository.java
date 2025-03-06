package project.expenseincomeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.Expense;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser_Username(String username);

    List<Expense> findByUser_UsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUser_UsernameAndExpenseCategory_ExpenseCategoryNameAndDateBetween(String username, String categoryName, LocalDate startDate, LocalDate endDate);

    boolean existsByUserAndName(User user, String name);

    boolean existsByUserAndExpenseCategory(User user, ExpenseCategory expenseCategory);

    List<Expense> findByUserUsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);
}
