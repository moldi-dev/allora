package com.moldi.allora.request.user;

import com.moldi.allora.validation.OrderRequestValidation;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@OrderRequestValidation
public record OrderRequest(
        @NotNull(message = "The order products are required")
        List<OrderLineProductRequest> orderLineProducts
) {
}
