package com.forwardagency.forwardbackend.Repo;

import com.forwardagency.forwardbackend.Model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserSessionRepo extends JpaRepository<UserSession, String> {

    Optional<UserSession> findByToken(String token);

    @Modifying
    @Query("delete from UserSession s where s.userEmail = :email")
    void deleteByUserEmail(@Param("email") String email);

    @Modifying
    @Query("delete from UserSession s where s.expiresAt <= :now")
    void deleteExpired(@Param("now") Instant now);
}
