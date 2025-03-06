package project.expenseincomeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.expenseincomeproject.model.BudgetPlan;
import project.expenseincomeproject.service.BudgetPlanService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/budget-plan")
@RequiredArgsConstructor
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;

    @PostMapping("/create")
    public ResponseEntity<String> createBudgetPlan(@RequestParam LocalDate startDate,
                                                   @RequestParam LocalDate endDate,
                                                   @RequestParam double plannedAmount) {
        String response = budgetPlanService.createBudgetPlan(startDate, endDate, plannedAmount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-plans")
    public ResponseEntity<List<BudgetPlan>> getUserBudgetPlans() {
        List<BudgetPlan> budgetPlans = budgetPlanService.getUserBudgetPlans();
        return ResponseEntity.ok(budgetPlans);
    }

    @GetMapping("/evaluate/{id}")
    public ResponseEntity<String> evaluateBudgetPlan(@PathVariable Long id) {
        String response = budgetPlanService.evaluateBudgetPlan(id);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBudgetPlan(@PathVariable Long id) {
        String response = budgetPlanService.deleteBudgetPlan(id);
        return ResponseEntity.ok(response);
    }
}