package project.expenseincomeproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.expenseincomeproject.dto.security.AuthResponse;
import project.expenseincomeproject.dto.security.LoginDTO;
import project.expenseincomeproject.dto.security.RegisterDTO;
import project.expenseincomeproject.service.security.AuthService;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*@Operation(summary = "Register a new user",
            description = "Allows a new user to register by providing necessary information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })*/
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterDTO request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*@Operation(summary = "User login", description = "Logs in a user by validating credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })*/
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
