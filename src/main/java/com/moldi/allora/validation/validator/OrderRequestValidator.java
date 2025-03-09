package com.moldi.allora.validation.validator;

import com.moldi.allora.request.user.OrderLineProductRequest;
import com.moldi.allora.request.user.OrderRequest;
import com.moldi.allora.validation.OrderRequestValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderRequestValidator implements ConstraintValidator<OrderRequestValidation, OrderRequest> {

    @Override
    public boolean isValid(OrderRequest orderRequest, ConstraintValidatorContext context) {
        if (orderRequest == null || orderRequest.orderLineProducts() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The order request is required").addConstraintViolation();
            return false;
        }

        for (OrderLineProductRequest productRequest : orderRequest.orderLineProducts()) {
            if (productRequest.productId() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The product is required").addConstraintViolation();
                return false;
            }

            if (productRequest.productSizeId() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The product size is required").addConstraintViolation();
                return false;
            }

            if (productRequest.quantity() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The product quantity is required").addConstraintViolation();
                return false;
            }

            if (productRequest.productId() <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The product id must be positive").addConstraintViolation();
                return false;
            }

            if (productRequest.quantity() <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The product quantity must be positive").addConstraintViolation();
                return false;
            }

            if (productRequest.productSizeId() <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The product size id must be positive").addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
