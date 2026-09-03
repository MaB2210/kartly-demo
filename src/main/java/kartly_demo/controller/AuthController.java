package kartly_demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kartly_demo.dto.LoginRequest;
import kartly_demo.dto.RegisterRequest;
import kartly_demo.entity.UserEntity;
import kartly_demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "User registration and login")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Hashes the password with BCrypt before saving. Rejects duplicate emails.")
    @PostMapping("/register")
    public UserEntity register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @Operation(summary = "Log in an existing user", description = "Verifies the submitted password against the stored BCrypt hash.")
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }
}
