package com.mycompany.property_management.service.impl;

import com.mycompany.property_management.convertor.PropertyConvertor;
import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.entity.PropertyEntity;
import com.mycompany.property_management.entity.Role;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.repo.PropertyRepo;
import com.mycompany.property_management.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepo propertyRepo;

    @Autowired
    private PropertyConvertor propertyConvertor;

    @Override
    public PropertyDTO saveProperty(PropertyDTO propertyDTO, UserEntity owner) {
        PropertyEntity propertyEntity = propertyConvertor.convertDTOtoEntity(propertyDTO, owner);
        propertyEntity = propertyRepo.save(propertyEntity);
        return propertyConvertor.convertEntityToDTO(propertyEntity);
    }

    @Override
    public List<PropertyDTO> getAllProperty() {
        return StreamSupport.stream(propertyRepo.findAll().spliterator(), false)
                .map(propertyConvertor::convertEntityToDTO)
                .toList();
    }

    @Override
    public List<?> getAllPropertiesForUser(UserEntity currentUser) {
        if (currentUser.getRole() == Role.OWNER) {
            return propertyRepo.findByOwnerId(currentUser.getId())
                    .stream()
                    .map(propertyConvertor::convertEntityToDTO)
                    .toList();
        } else {
            return propertyRepo.findAll()
                    .stream()
                    .map(propertyConvertor::convertEntityToPublicDTO)
                    .toList();
        }
    }

    /**
     * @return the entity if it exists AND belongs to requestingUserId, otherwise empty
     */
    private Optional<PropertyEntity> findOwnedProperty(Long propertyId, Long requestingUserId) {
        return propertyRepo.findById(propertyId)
                .filter(entity -> entity.getOwner() != null
                        && entity.getOwner().getId().equals(requestingUserId));
    }

    @Override
    public PropertyDTO updateProperty(PropertyDTO propertyDTO, Long propertyId, Long requestingUserId) {
        Optional<PropertyEntity> optEn = findOwnedProperty(propertyId, requestingUserId);
        if (optEn.isEmpty()) {
            return null;
        }
        PropertyEntity propertyEntity = optEn.get();
        propertyEntity.setTitle(propertyDTO.getTitle());
        propertyEntity.setAddress(propertyDTO.getAddress());
        propertyEntity.setPrice(propertyDTO.getPrice());
        propertyEntity.setDescription(propertyDTO.getDescription());
        propertyRepo.save(propertyEntity);
        return propertyConvertor.convertEntityToDTO(propertyEntity);
    }

    @Override
    public PropertyDTO updatePropertyDescription(PropertyDTO propertyDTO, Long propertyId, Long requestingUserId) {
        Optional<PropertyEntity> optEn = findOwnedProperty(propertyId, requestingUserId);
        if (optEn.isEmpty()) {
            return null;
        }
        PropertyEntity propertyEntity = optEn.get();
        propertyEntity.setDescription(propertyDTO.getDescription());
        propertyRepo.save(propertyEntity);
        return propertyConvertor.convertEntityToDTO(propertyEntity);
    }

    @Override
    public PropertyDTO updatePropertyPrice(PropertyDTO propertyDTO, Long propertyId, Long requestingUserId) {
        Optional<PropertyEntity> optEn = findOwnedProperty(propertyId, requestingUserId);
        if (optEn.isEmpty()) {
            return null;
        }
        PropertyEntity propertyEntity = optEn.get();
        propertyEntity.setPrice(propertyDTO.getPrice());
        propertyRepo.save(propertyEntity);
        return propertyConvertor.convertEntityToDTO(propertyEntity);
    }

    @Override
    public boolean deleteProperty(Long propertyId, Long requestingUserId) {
        Optional<PropertyEntity> optEn = findOwnedProperty(propertyId, requestingUserId);
        if (optEn.isEmpty()) {
            return false;
        }
        propertyRepo.delete(optEn.get());
        return true;
    }
}