package com.moldi_sams.se_project.response;

public record OrderLineProductResponse(
        Long orderLineProductId,
        ProductResponse product,
        Long quantity,
        ProductSizeResponse productSize
) {
}
