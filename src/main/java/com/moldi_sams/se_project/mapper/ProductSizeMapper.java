package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.ProductSize;
import com.moldi_sams.se_project.response.ProductSizeResponse;
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
