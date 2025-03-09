package com.moldi.allora.mapper;

import com.moldi.allora.entity.ProductCategory;
import com.moldi.allora.response.ProductCategoryResponse;
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
