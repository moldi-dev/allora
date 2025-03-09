package com.moldi.allora.response;

public record OrderLineProductResponse(
        Long orderLineProductId,
        ProductResponse product,
        Long quantity,
        ProductSizeResponse productSize
) {
}
