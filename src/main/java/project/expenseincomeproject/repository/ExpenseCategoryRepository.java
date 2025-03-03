package project.expenseincomeproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.ExpenseCategory;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByExpenseCategoryName(String expenseCategoryName);

    boolean existsByExpenseCategoryName(String expenseCategoryName);

}
