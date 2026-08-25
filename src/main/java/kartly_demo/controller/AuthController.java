package kartly_demo.controller;

import kartly_demo.dto.LoginRequest;
import kartly_demo.dto.RegisterRequest;
import kartly_demo.entity.UserEntity;
import kartly_demo.exception.DuplicateResourceException;
import kartly_demo.exception.InvalidCredentialsException;
import kartly_demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public UserEntity login(@RequestBody LoginRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPasswordHash())){
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }
}
