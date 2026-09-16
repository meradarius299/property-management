package com.mycompany.property_management.dto;

public record AuthRequest(String username, String password, String email, String role) {
}