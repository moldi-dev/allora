package com.moldi_sams.se_project.request.user;

import com.moldi_sams.se_project.validation.OrderRequestValidation;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@OrderRequestValidation
public record OrderRequest(
        @NotNull(message = "The order products are required")
        List<OrderLineProductRequest> orderLineProducts
) {
}
