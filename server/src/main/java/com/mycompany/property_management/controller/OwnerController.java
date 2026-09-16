package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.OwnerProfileDTO;
import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.convertor.PropertyConvertor;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.repo.PropertyRepo;
import com.mycompany.property_management.repo.UserRepo;
import com.mycompany.property_management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Public-facing owner profile: any authenticated user (owner or buyer)
 * can view an owner's name, email, and their full list of properties.
 * Used so buyers can click an owner's name and see everything they have listed.
 */
@RestController
@RequestMapping("/api/v1/owners")
public class OwnerController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PropertyRepo propertyRepo;

    @Autowired
    private PropertyConvertor propertyConvertor;

    @Autowired
    private AuthService authService;

    @GetMapping("/{ownerId}")
    public ResponseEntity<?> getOwnerProfile(@PathVariable Long ownerId,
                                             @RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> requesterOpt = authService.getUserByToken(authService.extractToken(authHeader));
        if (requesterOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }

        Optional<UserEntity> ownerOpt = userRepo.findById(ownerId);
        if (ownerOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Owner not found"), HttpStatus.NOT_FOUND);
        }

        UserEntity owner = ownerOpt.get();
        List<PropertyDTO> properties = propertyRepo.findByOwnerId(ownerId)
                .stream()
                .map(propertyConvertor::convertEntityToDTO)
                .toList();

        OwnerProfileDTO profile = new OwnerProfileDTO(owner.getId(), owner.getUsername(), owner.getEmail(), properties);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
