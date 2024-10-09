package com.moldi_sams.se_project.request.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotNull(message = "The product id is required")
        Long productId,

        @NotNull(message = "The rating is required")
        @Min(value = 1, message = "The minimum rating is 1 star")
        @Max(value = 5, message = "The maximum rating is 5 stars")
        Integer rating,

        String comment
) {
}
