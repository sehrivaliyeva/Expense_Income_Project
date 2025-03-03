package project.expenseincomeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.expenseincomeproject.model.IncomeCategory;
import project.expenseincomeproject.repository.IncomeCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeCategoryService {

    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;

    public IncomeCategory save(IncomeCategory category) {
        return incomeCategoryRepository.save(category);
    }

    public List<IncomeCategory> getAll() {
        return incomeCategoryRepository.findAll();
    }

    public IncomeCategory update(Long id, IncomeCategory updatedCategory) {
        Optional<IncomeCategory> optionalCategory = incomeCategoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            IncomeCategory existingCategory = optionalCategory.get();
            existingCategory.setIncomeCategoryName(updatedCategory.getIncomeCategoryName());
            return incomeCategoryRepository.save(existingCategory);
        }
        throw new RuntimeException("Income Category not found with id: " + id);
    }

    public void delete(Long id) {
        if (!incomeCategoryRepository.existsById(id)) {
            throw new RuntimeException("Income Category not found with id: " + id);
        }
        incomeCategoryRepository.deleteById(id);
    }
}
