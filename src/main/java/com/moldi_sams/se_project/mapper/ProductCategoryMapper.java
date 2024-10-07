package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.ProductCategory;
import com.moldi_sams.se_project.response.ProductCategoryResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryMapper {
    public ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory) {
        return new ProductCategoryResponse(
                productCategory.getProductCategoryId(),
                productCategory.getName()
        );
    }
}
