package project.expenseincomeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.expenseincomeproject.dto.IncomeCategoryRequestDto;
import project.expenseincomeproject.dto.IncomeCategoryResponseDto;
import project.expenseincomeproject.service.IncomeCategoryService;

import java.util.List;

@RestController
@RequestMapping("/v1/income-categories")
@RequiredArgsConstructor
public class IncomeCategoryController {

    private final IncomeCategoryService incomeCategoryService;

   @PostMapping("/add") ///void
   public ResponseEntity<IncomeCategoryResponseDto> createCategory(@RequestBody IncomeCategoryRequestDto requestDto) {
       return ResponseEntity.ok(incomeCategoryService.save(requestDto));
   }


    @GetMapping
    public ResponseEntity<List<IncomeCategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(incomeCategoryService.getAll());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<IncomeCategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody IncomeCategoryRequestDto requestDto) {
        return ResponseEntity.ok(incomeCategoryService.update(id, requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        incomeCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
