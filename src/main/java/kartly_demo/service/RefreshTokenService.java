package kartly_demo.service;

import kartly_demo.entity.RefreshTokenEntity;
import kartly_demo.entity.UserEntity;
import kartly_demo.exception.InvalidCredentialsException;
import kartly_demo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private static final long REFRESH_TOKEN_DURATION_DAYS = 7;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String createRefreshToken(UserEntity user){
        refreshTokenRepository.deleteByUser_Id(user.getId());

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_DURATION_DAYS));

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public UserEntity validateAndGetUser(String token){
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token."));

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidCredentialsException("Refresh token expired, please log in again");
        }
        return refreshToken.getUser();
    }
}
