package project.expenseincomeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.BudgetPlan;
import project.expenseincomeproject.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long> {
    List<BudgetPlan> findByUser(User user);

    Optional<BudgetPlan> findByUserUsernameAndStartDateAndEndDate(String username, LocalDate startDate, LocalDate endDate);
}
