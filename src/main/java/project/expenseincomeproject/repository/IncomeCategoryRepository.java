package project.expenseincomeproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.IncomeCategory;

import java.util.Optional;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    Optional<IncomeCategory> findByIncomeCategoryName(String incomeCategoryName);

    boolean existsByIncomeCategoryName(String incomeCategoryName);
}
