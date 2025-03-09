package com.moldi.allora.validation;

import com.moldi.allora.validation.validator.PriceRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PriceRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceRange {
    String message() default "Invalid price range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
