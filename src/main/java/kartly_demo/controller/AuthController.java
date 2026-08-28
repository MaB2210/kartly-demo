package kartly_demo.controller;

import jakarta.validation.Valid;
import kartly_demo.dto.LoginRequest;
import kartly_demo.dto.RegisterRequest;
import kartly_demo.entity.UserEntity;
import kartly_demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserEntity register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public UserEntity login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }
}
