package project.expenseincomeproject.repository;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.expenseincomeproject.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(@NotNull(message = "Username must not be null")
                             @Size(min = 6, max = 50, message = "Username must be between 3 and 50 characters")
                             String username);

}
