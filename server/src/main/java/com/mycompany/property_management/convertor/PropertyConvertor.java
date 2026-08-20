package com.mycompany.property_management.convertor;

import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.entity.PropertyEntity;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
public class PropertyConvertor {

    public PropertyEntity convertDTOtoEntity(PropertyDTO propertyDTO) {
        if (propertyDTO == null) return null;

        PropertyEntity pe = new PropertyEntity();
        pe.setTitle(propertyDTO.getTitle());
        pe.setAddress(propertyDTO.getAddress());
        pe.setOwnerEmail(propertyDTO.getOwnerEmail());
        pe.setOwnerName(propertyDTO.getOwnerName());
        pe.setPrice(propertyDTO.getPrice());
        pe.setDescription(propertyDTO.getDescription());

        return pe;
    }

    public PropertyDTO convertEntityToDTO(PropertyEntity pe) {
        if (pe == null) return null;

        return PropertyDTO.builder()
                .id(pe.getId())
                .title(pe.getTitle())
                .address(pe.getAddress())
                .ownerEmail(pe.getOwnerEmail())
                .ownerName(pe.getOwnerName())
                .price(pe.getPrice())
                .description(pe.getDescription())
                .build();
    }
}