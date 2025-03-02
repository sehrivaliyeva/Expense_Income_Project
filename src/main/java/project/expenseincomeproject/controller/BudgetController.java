package project.expenseincomeproject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

  /*  @Autowired
    private BudgetPlanRepository budgetPlanRepository;

    @Autowired
    private UserRepository userRepository;

    // Büdcə planı yaratmaq
    @PostMapping("/create")
    public ResponseEntity<BudgetPlan> createBudgetPlan(@RequestBody BudgetPlan budgetPlan) {
        User user = userRepository.findById(budgetPlan.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        budgetPlan.setUser(user);
        budgetPlanRepository.save(budgetPlan);
        return ResponseEntity.ok(budgetPlan);
    }

    // Büdcə planlarını izləmək
    @GetMapping("/status/{userId}")
    public ResponseEntity<List<String>> getUserBudgetStatus(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<BudgetPlan> userPlans = budgetPlanRepository.findByUser(user);
        List<String> planStatuses = userPlans.stream()
                .map(BudgetPlan::getPlanStatus)
                .collect(Collectors.toList());

        return ResponseEntity.ok(planStatuses);
    }

    // Büdcə planını güncəlləmək
    @PutMapping("/update/{id}")
    public ResponseEntity<BudgetPlan> updateBudgetPlan(@PathVariable Long id, @RequestBody BudgetPlan updatedPlan) {
        BudgetPlan existingPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget Plan not found"));

        existingPlan.setPlannedAmount(updatedPlan.getPlannedAmount());
        existingPlan.setStartDate(updatedPlan.getStartDate());
        existingPlan.setEndDate(updatedPlan.getEndDate());
        existingPlan.setTotalExpenses(updatedPlan.getTotalExpenses());

        budgetPlanRepository.save(existingPlan);
        return ResponseEntity.ok(existingPlan);
    }

    // Büdcə planını silmək
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudgetPlan(@PathVariable Long id) {
        BudgetPlan existingPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget Plan not found"));

        budgetPlanRepository.delete(existingPlan);
        return ResponseEntity.noContent().build();
    }*/
}
