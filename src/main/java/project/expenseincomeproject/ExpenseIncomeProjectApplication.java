package project.expenseincomeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class ExpenseIncomeProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseIncomeProjectApplication.class, args);
    }

}
