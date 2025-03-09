package com.moldi.allora.validation;

import com.moldi.allora.validation.validator.OrderRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OrderRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderRequestValidation {
    String message() default "Invalid order request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
