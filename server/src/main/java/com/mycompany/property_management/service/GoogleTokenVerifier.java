package com.mycompany.property_management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Verifies Google Sign-In ID tokens using Googles tokeninfo endpoint
 * Simple and dependency-free — good for small/demo projects
 * For high-traffic production apps, Google recommends verifying the
 * JWT signature locally instead (via the google-api-client library),
 * to avoid depending on a network call per login.
 */
@Service
public class GoogleTokenVerifier {

    @Value("${google.client.id}")
    private String googleClientId;

    private final RestTemplate restTemplate = new RestTemplate();

    public record GoogleUserInfo(String email, String name) {
    }

    /**
     * @return verified user info, or throws IllegalArgumentException if the token is invalid
     */
    @SuppressWarnings("unchecked")
    public GoogleUserInfo verify(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        Map<String, Object> claims;
        try {
            claims = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        if (claims == null) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        String audience = (String) claims.get("aud");
        if (!googleClientId.equals(audience)) {
            throw new IllegalArgumentException("Token was not issued for this application");
        }

        String emailVerified = (String) claims.get("email_verified");
        if (!"true".equals(emailVerified)) {
            throw new IllegalArgumentException("Google email is not verified");
        }

        String email = (String) claims.get("email");
        String name = (String) claims.getOrDefault("name", email);

        return new GoogleUserInfo(email, name);
    }
}
