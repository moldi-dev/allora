package com.moldi.allora.mapper;

import com.moldi.allora.entity.Product;
import com.moldi.allora.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final ImageMapper imageMapper;
    private final ProductSizeMapper productSizeMapper;
    private final ProductBrandMapper productBrandMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductGenderMapper productGenderMapper;

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getSizes().stream().map(productSizeMapper::toProductSizeResponse).toList(),
                productBrandMapper.toProductBrandResponse(product.getBrand()),
                productGenderMapper.toProductGenderResponse(product.getGender()),
                productCategoryMapper.toProductCategoryResponse(product.getCategory()),
                product.getImages().stream().map(imageMapper::toImageResponse).toList()
        );
    }
}
