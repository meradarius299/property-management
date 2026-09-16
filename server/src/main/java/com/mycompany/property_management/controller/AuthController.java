package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.AuthRequest;
import com.mycompany.property_management.dto.AuthResponse;
import com.mycompany.property_management.dto.GoogleAuthRequest;
import com.mycompany.property_management.entity.Role;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.service.AuthService;
import com.mycompany.property_management.service.GoogleTokenVerifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final GoogleTokenVerifier googleTokenVerifier;

    public AuthController(AuthService authService, GoogleTokenVerifier googleTokenVerifier) {
        this.authService = authService;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            Role role = Role.valueOf(request.role().toUpperCase());
            UserEntity user = authService.register(request.username(), request.password(), request.email(), role);
            return new ResponseEntity<>(Map.of("id", user.getId(), "username", user.getUsername(), "role", user.getRole()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<String> token = authService.login(request.username(), request.password());
        if (token.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = authService.getUserByToken(token.get()).orElseThrow();
        return new ResponseEntity<>(new AuthResponse(user.getId(), token.get(), user.getUsername(), user.getRole().name()), HttpStatus.OK);
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleAuthRequest request) {
        try {
            GoogleTokenVerifier.GoogleUserInfo googleUser = googleTokenVerifier.verify(request.idToken());
            String role = request.role() != null ? request.role() : "BUYER";
            String token = authService.loginOrRegisterWithGoogle(googleUser.email(), role);
            UserEntity user = authService.getUserByToken(token).orElseThrow();
            return new ResponseEntity<>(new AuthResponse(user.getId(), token, user.getUsername(), user.getRole().name()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
