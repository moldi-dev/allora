package com.moldi.allora.request.admin;

import com.moldi.allora.validation.StringValues;
import jakarta.validation.constraints.NotEmpty;

public record OrderUpdateRequest(
        @NotEmpty(message = "The order status is required")
        @StringValues(message = "The order status must be one of PENDING, PAID, DELIVERED", allowedValues = {"PENDING", "PAID", "DELIVERED"})
        String orderStatus
) {
}
