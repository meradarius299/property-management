package com.mycompany.property_management.service;

import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.entity.UserEntity;

import java.util.List;

public interface PropertyService {
    PropertyDTO saveProperty(PropertyDTO propertyDTO, UserEntity owner);
    List<?> getAllPropertiesForUser(UserEntity currentUser);
    List<PropertyDTO> getAllProperty();

    /**
     * @return updated DTO, or null if the property doesn't exist
     *         or doesn't belong to requestingUserId
     */
    PropertyDTO updateProperty(PropertyDTO propertyDTO, Long propertyId, Long requestingUserId);
    PropertyDTO updatePropertyDescription(PropertyDTO propertyDTO, Long propertyId, Long requestingUserId);
    PropertyDTO updatePropertyPrice(PropertyDTO propertyDTO, Long propertyId, Long requestingUserId);

    /**
     * @return true if deleted, false if not found or not owned by requestingUserId
     */
    boolean deleteProperty(Long propertyId, Long requestingUserId);
}
