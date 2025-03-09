package com.moldi.allora.request.user;

import jakarta.validation.constraints.NotNull;

public record OrderLineProductRequest(
        @NotNull(message = "The product id is required")
        Long productId,

        @NotNull(message = "The product quantity is required")
        Long quantity,

        @NotNull(message = "The product size is required")
        Long productSizeId
) {
}
