package project.expenseincomeproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.Income;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income,Long> {

    List<Income> findByIncomeCategoryIdAndDateBetween(Long categoryId, LocalDate startDate, LocalDate endDate);

    List<Income> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
