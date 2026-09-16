package com.mycompany.property_management.service;

import com.mycompany.property_management.entity.Role;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // token -> userId
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public AuthService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity register(String username, String rawPassword, String email, Role role) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }
        String hash = passwordEncoder.encode(rawPassword);
        UserEntity user = new UserEntity(username, hash, email, role);
        return userRepo.save(user);
    }

    public Optional<String> login(String username, String rawPassword) {
        Optional<UserEntity> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        UserEntity user = userOpt.get();
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return Optional.empty();
        }
        String token = UUID.randomUUID().toString();
        sessions.put(token, user.getId());
        return Optional.of(token);
    }

    /**
     * Login via Google. If no local account exists for this email yet,
     * creates one automatically with the requested role. If an account
     * already exists, logs into it and ignores the requested role
     * (role is fixed at first registration, same as normal accounts).
     */
    public String loginOrRegisterWithGoogle(String email, String requestedRole) {
        Optional<UserEntity> existing = userRepo.findByUsername(email);

        UserEntity user;
        if (existing.isPresent()) {
            user = existing.get();
        } else {
            Role role = Role.valueOf(requestedRole.toUpperCase());
            String randomPassword = new BigInteger(130, new SecureRandom()).toString(32);
            user = new UserEntity(email, passwordEncoder.encode(randomPassword), email, role);
            user = userRepo.save(user);
        }

        String token = UUID.randomUUID().toString();
        sessions.put(token, user.getId());
        return token;
    }

    public Optional<UserEntity> getUserByToken(String token) {
        if (token == null) {
            return Optional.empty();
        }
        Long userId = sessions.get(token);
        if (userId == null) {
            return Optional.empty();
        }
        return userRepo.findById(userId);
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return authorizationHeader;
    }

    public void logout(String token) {
        sessions.remove(token);
    }
}
