package project.expenseincomeproject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/income-categories")
public class IncomeCategoryController {

   /* @Autowired
    private IncomeCategoryService incomeCategoryService;

    @PostMapping("/add")
    public ResponseEntity<IncomeCategory> createCategory(@RequestBody IncomeCategory category) {
        return ResponseEntity.ok(incomeCategoryService.save(category));
    }

    @GetMapping("")
    public ResponseEntity<List<IncomeCategory>> getAllCategories() {
        return ResponseEntity.ok(incomeCategoryService.getAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<IncomeCategory> updateCategory(@PathVariable Long id, @RequestBody IncomeCategory category) {
        return ResponseEntity.ok(incomeCategoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        incomeCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }*/
}
