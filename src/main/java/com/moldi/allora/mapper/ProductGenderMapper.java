package com.moldi.allora.mapper;

import com.moldi.allora.entity.ProductGender;
import com.moldi.allora.response.ProductGenderResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductGenderMapper {
    public ProductGenderResponse toProductGenderResponse(ProductGender productGender) {
        return new ProductGenderResponse(
                productGender.getProductGenderId(),
                productGender.getName()
        );
    }
}
