package com.mycompany.property_management.dto;

import java.util.List;

public record OwnerProfileDTO(Long id, String username, String email, List<PropertyDTO> properties) {
}
