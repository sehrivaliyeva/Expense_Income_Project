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

import java.time.LocalDate;

@Entity
@Table(name = "incomes")
@Data
public class Income {
    /// premya  50.30.20 ve s

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate date;
    /// 2025-03-05  maas 50 azn premya

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private IncomeCategory incomeCategory;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
