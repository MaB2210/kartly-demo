package kartly_demo.service;

import kartly_demo.dto.AuthResponse;
import kartly_demo.dto.LoginRequest;
import kartly_demo.dto.RegisterRequest;
import kartly_demo.entity.UserEntity;
import kartly_demo.exception.DuplicateResourceException;
import kartly_demo.exception.InvalidCredentialsException;
import kartly_demo.repository.RefreshTokenRepository;
import kartly_demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserEntity register(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new DuplicateResourceException("Email already registered: "+request.getEmail());
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPasswordHash())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user.getEmail(),user.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshAccessToken(String refreshToken){
        UserEntity user = refreshTokenService.validateAndGetUser(refreshToken);
        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(newAccessToken, refreshToken);
    }

    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
    }
}
