package com.forwardagency.forwardbackend.Service;

import com.forwardagency.forwardbackend.Model.UserSession;
import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.Repo.UserRepo;
import com.forwardagency.forwardbackend.Repo.UserSessionRepo;
import com.forwardagency.forwardbackend.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthTokenService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppProperties appProperties;

    @Transactional
    public String issueToken(Users user) {
        Instant now = Instant.now();
        long ttlHours = Math.max(1L, appProperties.getAuth().getSessionTtlHours());

        UserSession session = new UserSession();
        session.setToken(generateToken());
        session.setUserEmail(user.getEmail());
        session.setCreatedAt(now);
        session.setExpiresAt(now.plus(Duration.ofHours(ttlHours)));
        userSessionRepo.save(session);
        return session.getToken();
    }

    @Transactional
    public Optional<Users> resolve(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        Optional<UserSession> sessionOpt = userSessionRepo.findByToken(token);
        if (sessionOpt.isEmpty()) {
            return Optional.empty();
        }

        UserSession session = sessionOpt.get();
        if (session.isExpired(Instant.now())) {
            userSessionRepo.delete(session);
            return Optional.empty();
        }

        Users user = userRepo.findByEmail(session.getUserEmail());
        if (user == null) {
            userSessionRepo.delete(session);
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Transactional
    public void revoke(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        userSessionRepo.findByToken(token).ifPresent(userSessionRepo::delete);
    }

    @Transactional
    public void revokeAllForUser(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        userSessionRepo.deleteByUserEmail(email);
    }

    private static String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
