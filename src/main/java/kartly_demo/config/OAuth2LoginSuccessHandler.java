package kartly_demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kartly_demo.entity.Role;
import kartly_demo.entity.UserEntity;
import kartly_demo.repository.UserRepository;
import kartly_demo.service.JwtService;
import kartly_demo.service.RefreshTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository,JwtService jwtService,RefreshTokenService refreshTokenService){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPasswordHash("OAUTH2_USER_NO_PASSWORD");
                    newUser.setRole(Role.CUSTOMER);
                    return userRepository.save(newUser);
                });

        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        response.sendRedirect("http://localhost:5173/oauth-callback?accessToken=" + accessToken + "&refreshToken=" +refreshToken);

    }
}
