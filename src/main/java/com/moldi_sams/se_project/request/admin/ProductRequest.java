package com.moldi_sams.se_project.request.admin;

import com.moldi_sams.se_project.validation.ImageList;
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
        List<String> sizesNames,

        @NotNull(message = "The product's brand is required")
        String brandName,

        @NotNull(message = "The product's gender is required")
        String genderName,

        @NotNull(message = "The product's category is required")
        String categoryName,

        @ImageList
        List<MultipartFile> images
) {
}
