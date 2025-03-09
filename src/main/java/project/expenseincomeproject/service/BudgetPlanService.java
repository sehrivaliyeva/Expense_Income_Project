package project.expenseincomeproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.ExpenseIncomeProjectApplication;
import project.expenseincomeproject.model.BudgetPlan;
import project.expenseincomeproject.model.Expense;
import project.expenseincomeproject.model.Income;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.BudgetPlanRepository;
import project.expenseincomeproject.repository.ExpenseRepository;
import project.expenseincomeproject.repository.IncomeRepository;
import project.expenseincomeproject.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetPlanService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BudgetPlanRepository budgetPlanRepository;

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String createBudgetPlan(LocalDate startDate, LocalDate endDate, double plannedAmount) {
        User user = getAuthenticatedUser();

        List<Income> incomes = incomeRepository.findByUserUsernameAndDateBetween(user.getUsername(), startDate, endDate);
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();

        user.setBalance(totalIncome);
        userRepository.save(user);

        if (plannedAmount > totalIncome) {
            return "Error: Planned amount exceeds available balance!";
        }

        BudgetPlan budgetPlan = new BudgetPlan();
        budgetPlan.setUser(user);
        budgetPlan.setPlannedAmount(plannedAmount);
        budgetPlan.setStartDate(startDate);
        budgetPlan.setEndDate(endDate);
        budgetPlan.setStatus("Created");
        budgetPlanRepository.save(budgetPlan);

        return "Budget plan successfully created!";
    }


    public String evaluateBudgetPlan(Long budgetPlanId) {
        User user = getAuthenticatedUser();
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Budget plan not found!"));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to access this budget plan!");
        }

        LocalDate startDate = budgetPlan.getStartDate();
        LocalDate endDate = budgetPlan.getEndDate();
        double plannedAmount = budgetPlan.getPlannedAmount();


        List<Expense> expenses = expenseRepository.findByUserUsernameAndDateBetween(user.getUsername(), startDate, endDate);
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        long days = endDate.toEpochDay() - startDate.toEpochDay() + 1;
        double dailyExpenseRate = totalExpenses / days;
        double projectedSpending = dailyExpenseRate * days;

        String status = projectedSpending > plannedAmount ? "Negative" : "Positive";
        budgetPlan.setStatus(status);
        budgetPlanRepository.save(budgetPlan);

        return status.equals("Negative") ?
                "Negative: Your spending rate is higher than your plan. Consider adjusting expenses." :
                "Positive: Your plan is within your estimated spending. Keep it up!";
    }

    public List<BudgetPlan> getUserBudgetPlans() {
        User user = getAuthenticatedUser();
        return budgetPlanRepository.findByUser(user);
    }

    public String deleteBudgetPlan(Long budgetPlanId) {
        User user = getAuthenticatedUser();

        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Budget plan not found!"));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to delete this budget plan!");
        }

        budgetPlanRepository.delete(budgetPlan);

        return "Budget plan successfully deleted!";
    }
   // ExpenseIncomeProject-0.0.1-SNAPSHOT.jar
    //ExpenseIncomeProject-0.0.1-SNAPSHOT-plain.jar

}
