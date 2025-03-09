package com.moldi.allora.mapper;

import com.moldi.allora.entity.ProductSize;
import com.moldi.allora.response.ProductSizeResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductSizeMapper {
    public ProductSizeResponse toProductSizeResponse(ProductSize productSize) {
        return new ProductSizeResponse(
                productSize.getProductSizeId(),
                productSize.getName()
        );
    }
}
