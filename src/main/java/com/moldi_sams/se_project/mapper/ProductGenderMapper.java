package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.ProductGender;
import com.moldi_sams.se_project.response.ProductGenderResponse;
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
