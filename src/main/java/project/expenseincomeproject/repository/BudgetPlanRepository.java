package project.expenseincomeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.BudgetPlan;
import project.expenseincomeproject.model.User;

import java.util.List;

@Repository
public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long> {
    List<BudgetPlan> findByUser(User user);
}
