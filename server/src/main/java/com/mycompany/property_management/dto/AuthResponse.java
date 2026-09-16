package com.mycompany.property_management.dto;

public record AuthResponse(Long id, String token, String username, String role) {
}