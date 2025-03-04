package project.expenseincomeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.model.ExpenseCategory;
import project.expenseincomeproject.repository.ExpenseCategoryRepository;


import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseCategory save(ExpenseCategory category) {
        return expenseCategoryRepository.save(category);
    }

    public List<ExpenseCategory> getAll() {
        return expenseCategoryRepository.findAll();
    }

    public ExpenseCategory update(Long id, ExpenseCategory updatedCategory) {
        Optional<ExpenseCategory> optionalCategory = expenseCategoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            ExpenseCategory existingCategory = optionalCategory.get();
            
            if (expenseCategoryRepository.existsByExpenseCategoryName(updatedCategory.getExpenseCategoryName())
                    && !existingCategory.getExpenseCategoryName().equals(updatedCategory.getExpenseCategoryName())) {
                throw new RuntimeException("Expense Category with name '" + updatedCategory.getExpenseCategoryName() + "' already exists.");
            }

            existingCategory.setExpenseCategoryName(updatedCategory.getExpenseCategoryName());
            return expenseCategoryRepository.save(existingCategory);
        }

        throw new RuntimeException("Expense Category not found with id: " + id);
    }

    public void delete(Long id) {
        if (!expenseCategoryRepository.existsById(id)) {
            throw new RuntimeException("Expense Category not found with id: " + id);
        }
        expenseCategoryRepository.deleteById(id);
    }
}
