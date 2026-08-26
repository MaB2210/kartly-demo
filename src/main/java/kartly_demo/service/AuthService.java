package kartly_demo.service;

import kartly_demo.dto.LoginRequest;
import kartly_demo.dto.RegisterRequest;
import kartly_demo.entity.UserEntity;
import kartly_demo.exception.DuplicateResourceException;
import kartly_demo.exception.InvalidCredentialsException;
import kartly_demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity register(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new DuplicateResourceException("Email already regitered: "+request.getEmail());
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public UserEntity login(LoginRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPasswordHash())){
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }
}
