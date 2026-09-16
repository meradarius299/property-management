package com.mycompany.property_management.repo;

import com.mycompany.property_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}