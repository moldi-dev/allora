package com.moldi_sams.se_project.validation.validator;

import com.moldi_sams.se_project.request.user.ProductFilterRequest;
import com.moldi_sams.se_project.validation.PriceRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;


public class PriceRangeValidator implements ConstraintValidator<PriceRange, ProductFilterRequest> {

    @Override
    public boolean isValid(ProductFilterRequest request, ConstraintValidatorContext context) {
        if (request.minPrice() != null && request.maxPrice() != null) {
            if (request.minPrice().compareTo(request.maxPrice()) > 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The minimum price cannot be greater than the maximum price")
                        .addPropertyNode("minPrice")
                        .addConstraintViolation();
                return false;
            }
        }

        if ((request.minPrice() != null && request.minPrice().compareTo(BigDecimal.ZERO) <= 0) ||
                (request.maxPrice() != null && request.maxPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The minimum price must be positive")
                    .addPropertyNode("minPrice")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("The maximum price must be positive")
                    .addPropertyNode("maxPrice")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
