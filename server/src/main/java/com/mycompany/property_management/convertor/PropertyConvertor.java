//package com.mycompany.property_management.convertor;
//
//import com.mycompany.property_management.dto.PropertyDTO;
//import com.mycompany.property_management.dto.PropertyPublicDTO;
//import com.mycompany.property_management.entity.PropertyEntity;
//import com.mycompany.property_management.entity.UserEntity;
//import lombok.*;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PropertyConvertor {
//
//    public PropertyEntity convertDTOtoEntity(PropertyDTO propertyDTO, UserEntity owner) {
//        if (propertyDTO == null) return null;
//
//        PropertyEntity pe = new PropertyEntity();
//        pe.setTitle(propertyDTO.getTitle());
//        pe.setAddress(propertyDTO.getAddress());
//        pe.setPrice(propertyDTO.getPrice());
//        pe.setDescription(propertyDTO.getDescription());
//        pe.setOwner(owner);
//
//        return pe;
//    }
//
//    public PropertyDTO convertEntityToDTO(PropertyEntity pe) {
//        if (pe == null) return null;
//
//        return PropertyDTO.builder()
//                .id(pe.getId())
//                .title(pe.getTitle())
//                .address(pe.getAddress())
//                .ownerEmail(pe.getOwner() != null ? pe.getOwner().getEmail() : null)
//                .ownerName(pe.getOwner() != null ? pe.getOwner().getUsername() : null)
//                .price(pe.getPrice())
//                .description(pe.getDescription())
//                .build();
//    }
//
//    public PropertyPublicDTO convertEntityToPublicDTO(PropertyEntity pe) {
//        if (pe == null) return null;
//
//        return PropertyPublicDTO.builder()
//                .id(pe.getId())
//                .title(pe.getTitle())
//                .address(pe.getAddress())
//                .price(pe.getPrice())
//                .description(pe.getDescription())
//                .ownerId(pe.getOwner() != null ? pe.getOwner().getId() : null)
//                .ownerName(pe.getOwner() != null ? pe.getOwner().getUsername() : null)
//                .build();
//    }
//}
package com.mycompany.property_management.convertor;

import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.dto.PropertyPublicDTO;
import com.mycompany.property_management.entity.PropertyEntity;
import com.mycompany.property_management.entity.UserEntity;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
public class PropertyConvertor {

    public PropertyEntity convertDTOtoEntity(PropertyDTO propertyDTO, UserEntity owner) {
        if (propertyDTO == null) return null;

        PropertyEntity pe = new PropertyEntity();
        pe.setTitle(propertyDTO.getTitle());
        pe.setAddress(propertyDTO.getAddress());
        pe.setPrice(propertyDTO.getPrice());
        pe.setDescription(propertyDTO.getDescription());
        pe.setOwner(owner);

        return pe;
    }

    public PropertyDTO convertEntityToDTO(PropertyEntity pe) {
        if (pe == null) return null;

        return PropertyDTO.builder()
                .id(pe.getId())
                .title(pe.getTitle())
                .address(pe.getAddress())
                .ownerEmail(pe.getOwner() != null ? pe.getOwner().getEmail() : null)
                .ownerName(pe.getOwner() != null ? pe.getOwner().getUsername() : null)
                .price(pe.getPrice())
                .description(pe.getDescription())
                .build();
    }

    public PropertyPublicDTO convertEntityToPublicDTO(PropertyEntity pe) {
        if (pe == null) return null;

        return PropertyPublicDTO.builder()
                .id(pe.getId())
                .title(pe.getTitle())
                .address(pe.getAddress())
                .price(pe.getPrice())
                .description(pe.getDescription())
                .ownerId(pe.getOwner() != null ? pe.getOwner().getId() : null)
                .ownerName(pe.getOwner() != null ? pe.getOwner().getUsername() : null)
                .build();
    }
}
