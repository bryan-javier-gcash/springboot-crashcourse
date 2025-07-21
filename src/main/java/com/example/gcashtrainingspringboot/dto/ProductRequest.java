package com.example.gcashtrainingspringboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long id;

    @NotNull(message = "Product name should not be blank")
    private String productName;

    @Positive(message = "Product price should be a positive amount")
    private Double productPrice;
}
