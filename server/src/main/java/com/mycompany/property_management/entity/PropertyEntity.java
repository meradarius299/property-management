package com.mycompany.property_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PROPERTY_TABLE")
@Getter
@Setter
@NoArgsConstructor
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PROPERTY_TITLE", nullable = false, length = 255)
    private String title;

    private String description;
    private Double price;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private UserEntity owner;
}