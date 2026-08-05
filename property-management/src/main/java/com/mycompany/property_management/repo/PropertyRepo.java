package com.mycompany.property_management.repo;

import com.mycompany.property_management.entity.PropertyEntity;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepo extends CrudRepository<PropertyEntity, Long> {
}
