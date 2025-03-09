package com.moldi.allora.request.admin;

import com.moldi.allora.validation.ImageList;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotEmpty(message = "The product's name is required")
        @Size(max = 100, message = "The product's name can contain at most 100 characters")
        String name,

        @NotEmpty(message = "The product's description is required")
        @Size(max = 1000, message = "The product's description can contain at most 1000 characters")
        String description,

        @NotNull(message = "The product's price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "The product's price must be positive")
        BigDecimal price,

        @NotNull(message = "The product's stock is required")
        @Min(value = 0, message = "The product's stock must be positive or 0")
        Long stock,

        @NotNull(message = "The product's available size(s) is/are required")
        List<@NotEmpty @Size(max = 10, message = "The product's size can contain at most 10 characters") String> sizesNames,

        @NotEmpty(message = "The product's brand is required")
        @Size(max = 100, message = "The product's brand can contain at most 100 characters")
        String brandName,

        @NotEmpty(message = "The product's gender is required")
        @Size(max = 100, message = "The product's gender can contain at most 100 characters")
        String genderName,

        @NotEmpty(message = "The product's category is required")
        @Size(max = 100, message = "The product's category can contain at most 100 characters")
        String categoryName,

        @ImageList
        List<MultipartFile> images
) {
}
