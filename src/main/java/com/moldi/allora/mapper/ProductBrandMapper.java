package com.moldi.allora.mapper;

import com.moldi.allora.entity.ProductBrand;
import com.moldi.allora.response.ProductBrandResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductBrandMapper {
    public ProductBrandResponse toProductBrandResponse(ProductBrand productBrand) {
        return new ProductBrandResponse(
                productBrand.getProductBrandId(),
                productBrand.getName()
        );
    }
}
