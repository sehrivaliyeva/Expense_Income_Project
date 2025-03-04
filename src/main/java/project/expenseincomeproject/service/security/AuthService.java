package project.expenseincomeproject.service.security;

import project.expenseincomeproject.dto.security.AuthResponse;
import project.expenseincomeproject.dto.security.LoginDTO;
import project.expenseincomeproject.dto.security.RegisterDTO;

public interface AuthService {
    AuthResponse register(RegisterDTO request);
    AuthResponse login(LoginDTO request);

}
