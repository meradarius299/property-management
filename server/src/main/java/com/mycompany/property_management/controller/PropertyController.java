package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.entity.Role;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.service.AuthService;
import com.mycompany.property_management.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//RESTFULL API is maping of a URL to a java class func
//http://localhost:8080/api/v1/properties


//@Value("${pms.dummy:}")
//private String dummy;
//
//@Value("${spring.datasource.url:}")
//private String dbUrl;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private AuthService authService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProperty(@RequestBody PropertyDTO propertyDTO,
                                          @RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> userOpt = authService.getUserByToken(authService.extractToken(authHeader));
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        UserEntity owner = userOpt.get();
        if (owner.getRole() != Role.OWNER) {
            return new ResponseEntity<>(Map.of("error", "Only owners can add properties"), HttpStatus.FORBIDDEN);
        }
        PropertyDTO saved = propertyService.saveProperty(propertyDTO, owner);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /*@GetMapping
    public ResponseEntity getAllProperties(){
        System.out.println(dummy);
        System.out.println(dbUrl);
        List<PropertyDTO> propertyList = propertyService.getAllProperty();
        ResponseEntity<List<PropertyDTO>> responseEntity = new ResponseEntity<>(propertyList, HttpStatus.OK);
        return responseEntity;
    }*/

    private Optional<UserEntity> currentUser(String authHeader) {
        return authService.getUserByToken(authService.extractToken(authHeader));
    }

    @GetMapping
    public ResponseEntity<?> getAllProperties(@RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> userOpt = currentUser(authHeader);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        List<?> properties = propertyService.getAllPropertiesForUser(userOpt.get());
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<?> updateProperty(@RequestBody PropertyDTO propertyDTO,
                                            @PathVariable Long propertyId,
                                            @RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> userOpt = currentUser(authHeader);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        PropertyDTO updated = propertyService.updateProperty(propertyDTO, propertyId, userOpt.get().getId());
        if (updated == null) {
            return new ResponseEntity<>(Map.of("error", "Property not found or not owned by you"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/update_description/{propertyId}")
    public ResponseEntity<?> updatePropertyDescription(@RequestBody PropertyDTO propertyDTO,
                                                       @PathVariable Long propertyId,
                                                       @RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> userOpt = currentUser(authHeader);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        PropertyDTO updated = propertyService.updatePropertyDescription(propertyDTO, propertyId, userOpt.get().getId());
        if (updated == null) {
            return new ResponseEntity<>(Map.of("error", "Property not found or not owned by you"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/update_price/{propertyId}")
    public ResponseEntity<?> updatePropertyPrice(@RequestBody PropertyDTO propertyDTO,
                                                 @PathVariable Long propertyId,
                                                 @RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> userOpt = currentUser(authHeader);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        PropertyDTO updated = propertyService.updatePropertyPrice(propertyDTO, propertyId, userOpt.get().getId());
        if (updated == null) {
            return new ResponseEntity<>(Map.of("error", "Property not found or not owned by you"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long propertyId,
                                            @RequestHeader("Authorization") String authHeader) {
        Optional<UserEntity> userOpt = currentUser(authHeader);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        boolean deleted = propertyService.deleteProperty(propertyId, userOpt.get().getId());
        if (!deleted) {
            return new ResponseEntity<>(Map.of("error", "Property not found or not owned by you"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

