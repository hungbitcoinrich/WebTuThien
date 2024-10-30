package com.example.WebBanSach.repository;

import com.example.WebBanSach.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
    Optional<PasswordResetToken> findByEmailAndToken(String email, String token);
}
