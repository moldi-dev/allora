package com.moldi_sams.se_project.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotEmpty(message = "The product's name is required")
        @Size(max = 100, message = "The product's name can contain at most 100 characters")
        String productName,

        @NotEmpty(message = "The product's description is required")
        @Size(max = 255, message = "The product's description can contain at most 255 characters")
        String productDescription,

        @NotNull(message = "The product's price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "The product's price must be positive")
        BigDecimal productPrice
) {
}
