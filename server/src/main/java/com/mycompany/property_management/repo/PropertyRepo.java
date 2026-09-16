package com.mycompany.property_management.repo;

import com.mycompany.property_management.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepo extends JpaRepository<PropertyEntity, Long> {
    List<PropertyEntity> findByOwnerId(Long ownerId);
}