package project.expenseincomeproject.service.security.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.expenseincomeproject.dto.security.AuthResponse;
import project.expenseincomeproject.dto.security.LoginDTO;
import project.expenseincomeproject.dto.security.RegisterDTO;
import project.expenseincomeproject.enums.Role;
import project.expenseincomeproject.exception.ResourceNotFoundException;
import project.expenseincomeproject.model.User;
import project.expenseincomeproject.repository.UserRepository;
import project.expenseincomeproject.service.security.AuthService;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    /// herbir register olan usere biz ozumuz default olaraq rolunu USER teyin edirik
    @Override
    public AuthResponse register(RegisterDTO request) {
        User user = new User();
        user.setRole(Role.USER);
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setBalance(0.0);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new AuthResponse("User register successfully !!");
    }

    @Override
    public AuthResponse login(LoginDTO request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Invalid username or password. Please check your credentials and try again.");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid username or password"));

        String accessToken = jwtService.generateToken(user);

        return new AuthResponse(accessToken);
    }
    ///expense-income-app-plain.jar
    ///ExpenseIncomeProject-0.0.1-SNAPSHOT.jar

}
