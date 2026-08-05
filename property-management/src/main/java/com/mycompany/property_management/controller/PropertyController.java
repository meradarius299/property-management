package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.PropertyDTO;
import com.mycompany.property_management.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    //RESTFULL API is mapping of a URL to a java class func
    //http://localhost:8080/api/v1/properties

    @Value("${pms.dummy:}")
    private String dummy;

    @Value("${spring.datasource.url:}")
    private String dbUrl;

    @PostMapping("/save")
    public ResponseEntity saveProperty(@RequestBody PropertyDTO propertyDTO) {
        propertyDTO = propertyService.saveProperty(propertyDTO);
        ResponseEntity<PropertyDTO> responseEntity = new ResponseEntity<PropertyDTO>(propertyDTO, HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity getAllProperties(){
        System.out.println(dummy);
        System.out.println(dbUrl);
        List<PropertyDTO> propertyList =  propertyService.getAllProperty();
        ResponseEntity<List<PropertyDTO>> responseEntity = new ResponseEntity<>(propertyList, HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("{propertyId}")
    public ResponseEntity<PropertyDTO> updateProperty(@RequestBody PropertyDTO propertyDTO, @PathVariable Long propertyId) {
        propertyDTO = propertyService.updateProperty(propertyDTO, propertyId);
        ResponseEntity<PropertyDTO> responseEntity = new ResponseEntity<>(propertyDTO, HttpStatus.CREATED);
        return responseEntity;
    }

    @PatchMapping("/update_description/{propertyId}")
    public ResponseEntity<PropertyDTO> updatePropertyDescription(@RequestBody PropertyDTO propertyDTO, @PathVariable Long propertyId) {
        propertyDTO = propertyService.updatePropertyDescription(propertyDTO, propertyId);
        ResponseEntity<PropertyDTO> responseEntity = new ResponseEntity<>(propertyDTO, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/update_price/{propertyId}")
    public ResponseEntity<PropertyDTO> updatePropertyPrice(@RequestBody PropertyDTO propertyDTO, @PathVariable Long propertyId) {
        propertyDTO = propertyService.updatePropertyPrice(propertyDTO, propertyId);
        ResponseEntity<PropertyDTO> responseEntity = new ResponseEntity<>(propertyDTO, HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity deleteProperty(@PathVariable Long propertyId) {
        propertyService.deleteProperty(propertyId);
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }
}
