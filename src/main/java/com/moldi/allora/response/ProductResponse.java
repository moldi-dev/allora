package com.moldi.allora.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long productId,
        String name,
        String description,
        BigDecimal price,
        Long stock,
        List<ProductSizeResponse> sizes,
        ProductBrandResponse brand,
        ProductGenderResponse gender,
        ProductCategoryResponse category,
        List<ImageResponse> images
) {
}
