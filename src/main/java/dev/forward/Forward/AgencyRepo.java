package dev.forward.Forward;

import dev.forward.Forward.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyRepo extends JpaRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);
}
