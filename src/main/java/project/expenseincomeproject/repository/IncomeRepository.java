package project.expenseincomeproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.Income;
import project.expenseincomeproject.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income,Long> {
    
    List<Income> findByUser_UsernameAndIncomeCategory_IncomeCategoryNameAndDateBetween(String username, String categoryName, LocalDate startDate, LocalDate endDate);

    boolean existsByUserAndName(User user, String name);

    List<Income> findByUser_Username(String username);

    List<Income> findByUser_UsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);

    List<Income> findByUserUsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);
}
