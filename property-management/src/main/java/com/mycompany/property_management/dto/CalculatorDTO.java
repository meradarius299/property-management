package com.mycompany.property_management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculatorDTO {
    private Double a;
    private Double b;
    private Double c;
    @JsonProperty("num4")
    private Double d;
}
