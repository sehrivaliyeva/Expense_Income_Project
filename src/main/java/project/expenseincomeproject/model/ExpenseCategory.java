package project.expenseincomeproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "expensecategories")
@Data
public class ExpenseCategory { /// komunal -- su, isiq, kiraye  , neqliyyat -- benzin, metro,avtobus

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;


    private String expenseCategoryName;  // Məsələn: "Qida", "Nəqliyyat"

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}



