package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.Product;
import com.moldi_sams.se_project.response.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getProductPrice()
        );
    }
}
