package kartly_demo.repository;

import kartly_demo.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUser_Id(Long userId);
}
