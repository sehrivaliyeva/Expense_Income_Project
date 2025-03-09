package project.expenseincomeproject.repository;


import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    Optional<IncomeCategory> findByIncomeCategoryName(String incomeCategoryName);


    List<IncomeCategory> findByUser(User user);

    Optional<IncomeCategory> findByIncomeCategoryNameAndUser(@NotBlank(message = "Category name cannot be empty") String categoryName, User user);

    boolean existsByIncomeCategoryName(String categoryName);
}
