package project.expenseincomeproject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

   /* @Autowired
    private ReportService reportService;

    // a. İki tarix aralığında, həm gəlir, həm də xərcləri qaytaran API
    @GetMapping("/income-expense")
    public ResponseEntity<ReportDTO> getIncomeAndExpenseReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(reportService.getIncomeAndExpenseReport(startDate, endDate));
    }

    // b. Ümumi cari balansı qaytaran API
    @GetMapping("/current-balance")
    public ResponseEntity<Double> getCurrentBalance(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reportService.getCurrentBalance(user));
    }

    // c. Verilmiş tarixdə günün sonunda, qalıq balansı qaytaran API
    @GetMapping("/balance-at-date")
    public ResponseEntity<Double> getBalanceAtDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(reportService.getBalanceAtDate(date));
    }*/
}
