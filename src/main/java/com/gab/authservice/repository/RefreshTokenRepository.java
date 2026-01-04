package com.gab.authservice.repository;

import com.gab.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gab.authservice.entity.User;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
