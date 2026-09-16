package com.mycompany.property_management.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyPublicDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String address;
    private Long ownerId;
    private String ownerName;
}
