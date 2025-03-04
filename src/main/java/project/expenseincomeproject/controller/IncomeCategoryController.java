package project.expenseincomeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.service.IncomeCategoryService;

import java.util.List;

@RestController
@RequestMapping("/v1/income-categories")
@RequiredArgsConstructor
public class IncomeCategoryController {


    private final IncomeCategoryService incomeCategoryService;

    @PostMapping("/add")
    public ResponseEntity<IncomeCategory> createCategory(@RequestBody IncomeCategory category) {
        return ResponseEntity.ok(incomeCategoryService.save(category));
    }

    @GetMapping()
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
    }
}
