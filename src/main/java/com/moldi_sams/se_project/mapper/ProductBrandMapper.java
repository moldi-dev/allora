package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.ProductBrand;
import com.moldi_sams.se_project.response.ProductBrandResponse;
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
