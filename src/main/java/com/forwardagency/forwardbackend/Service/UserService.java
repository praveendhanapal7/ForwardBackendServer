package com.forwardagency.forwardbackend.Service;

import com.forwardagency.forwardbackend.Model.AccessModel;
import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.Repo.AccessModelRepo;
import com.forwardagency.forwardbackend.Repo.UserRepo;
import com.forwardagency.forwardbackend.exception.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccessModelService accessModelService;

    @Autowired
    private AccessModelRepo accessModelRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Users addUser(Users user) {

        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (user.getSecretKey() == null || user.getSecretKey().isBlank()) {
            throw new IllegalArgumentException("Access code is required");
        }

        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("User already exists");
        }

        String brandName = user.getBrandName();
        AccessModel existingAccessModel = accessModelRepo.getAccessModelBySecretKey(user.getSecretKey());

        if (brandName != null && !brandName.isBlank()) {
            if (existingAccessModel == null) {
                AccessModel newAccessModel = new AccessModel();
                newAccessModel.setSecretKey(user.getSecretKey());
                newAccessModel.setBrandName(brandName);
                accessModelRepo.save(newAccessModel);
            } else if (!brandName.equals(existingAccessModel.getBrandName())) {
                throw new IllegalStateException("Access code already belongs to another brand");
            }
        } else {
            brandName = accessModelService.getBrandName(user.getSecretKey());
        }

        String accountType = normalizeAccountType(user.getAccountType(), brandName);

        if (("staff".equals(accountType) || "agency".equals(accountType))
                && (brandName == null || brandName.isBlank())) {
            throw new IllegalArgumentException("Access code is invalid");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getEmail());
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            user.setPhoneNumber("NA");
        }

        user.setAccountType(accountType);
        user.setBrandName(brandName);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userRepo.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException("Unable to create user with the provided details");
        }
    }

    public Users authUser(Users user) {
        if (user == null
                || user.getEmail() == null
                || user.getEmail().isBlank()
                || user.getPassword() == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        Users dbUser = userRepo.findByEmail(user.getEmail());
        if (dbUser == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String raw = user.getPassword();
        String stored = dbUser.getPassword();

        if (stored == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (isBcryptHash(stored) && passwordEncoder.matches(raw, stored)) {
            return ensureNormalizedAccountType(dbUser);
        }

        if (isBcryptHash(stored)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (stored.equals(raw)) {
            dbUser.setPassword(passwordEncoder.encode(raw));
            log.info("Migrated user password to BCrypt: {}", user.getEmail());
            return ensureNormalizedAccountType(userRepo.save(dbUser));
        }

        throw new InvalidCredentialsException("Invalid email or password");
    }

    private Users ensureNormalizedAccountType(Users user) {
        String normalized = normalizeAccountType(user.getAccountType(), user.getBrandName());
        if (!normalized.equals(user.getAccountType())) {
            user.setAccountType(normalized);
            return userRepo.save(user);
        }
        return user;
    }

    static String normalizeAccountType(String raw, String brandName) {
        String key = raw == null ? "" : raw.trim().toLowerCase();
        return switch (key) {
            case "agency", "agency staff" -> "agency";
            case "staff" -> "staff";
            case "client", "brand owner" -> "client";
            case "" -> (brandName != null && !brandName.isBlank()) ? "client" : "staff";
            default -> "client";
        };
    }

    private static boolean isBcryptHash(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}
