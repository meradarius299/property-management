package com.mycompany.property_management.service.impl;

import com.mycompany.property_management.convertor.PropertyConvertor;
import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.entity.PropertyEntity;
import com.mycompany.property_management.repo.PropertyRepo;
import com.mycompany.property_management.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepo propertyRepo;

    @Autowired
    private PropertyConvertor propertyConvertor;

    @Value("${pms.dummy:}")
    private String dummy;

    @Value("${spring.datasource.url:}")
    private String dbUrl;

    @Override
    public PropertyDTO saveProperty(PropertyDTO propertyDTO) {

        PropertyEntity propertyEntity = propertyConvertor.convertDTOtoEntity(propertyDTO);
        propertyEntity = propertyRepo.save(propertyEntity);

        PropertyDTO propertyDTO1 = propertyConvertor.convertEntityToDTO(propertyEntity);
        return propertyDTO1;
    }

    @Override
    public List<PropertyDTO> getAllProperty() {
        System.out.println("Inside service: " + dummy);
        System.out.println("Inside service: " + dbUrl);
        return StreamSupport.stream(propertyRepo.findAll().spliterator(), false)
                .map(propertyConvertor::convertEntityToDTO)
                .toList();
    }

    @Override
    public PropertyDTO updateProperty(PropertyDTO propertyDTO, Long propertyId) {
        Optional<PropertyEntity> optEn = propertyRepo.findById(propertyId);
        PropertyDTO propertyDTO1 = null;
        if(optEn.isPresent()){

            PropertyEntity propertyEntity = optEn.get();
            propertyEntity.setTitle(propertyDTO.getTitle());
            propertyEntity.setAddress(propertyDTO.getAddress());
            propertyEntity.setOwnerEmail(propertyDTO.getOwnerEmail());
            propertyEntity.setOwnerName(propertyDTO.getOwnerName());
            propertyEntity.setPrice(propertyDTO.getPrice());
            propertyEntity.setDescription(propertyDTO.getDescription());
            propertyDTO1 = propertyConvertor.convertEntityToDTO(propertyEntity);
            propertyRepo.save(propertyEntity);
        }
        return propertyDTO1;
    }

    @Override
    public PropertyDTO updatePropertyDescription(PropertyDTO propertyDTO, Long propertyId) {
        Optional<PropertyEntity> optEn = propertyRepo.findById(propertyId);
        PropertyDTO propertyDTO1 = null;
        if(optEn.isPresent()){

            PropertyEntity propertyEntity = optEn.get(); //data from DB
            propertyEntity.setDescription(propertyDTO.getDescription());
            propertyDTO1 = propertyConvertor.convertEntityToDTO(propertyEntity);
            propertyRepo.save(propertyEntity);
        }
        return propertyDTO1;

    }

    @Override
    public PropertyDTO updatePropertyPrice(PropertyDTO propertyDTO, Long propertyId) {
        Optional<PropertyEntity> optEn = propertyRepo.findById(propertyId);
        PropertyDTO propertyDTO1 = null;
        if(optEn.isPresent()){
            PropertyEntity propertyEntity = optEn.get(); //data from DB
            propertyEntity.setPrice(propertyDTO.getPrice());
            propertyRepo.save(propertyEntity);
        }
        return propertyDTO1;
    }

    @Override
    public void deleteProperty(Long propertyId) {
        propertyRepo.deleteById(propertyId);
    }
}
