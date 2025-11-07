package com.monitoramento.user.infrastructure.persistence;

import com.monitoramento.user.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(UUID userId);

    void deleteByExpiryDateBefore(OffsetDateTime date);

    Optional<RefreshToken> findByUserIdAndRevokedFalse(UUID userId);
}
