package com.moldi_sams.se_project.validation;

import com.moldi_sams.se_project.validation.validator.ImageListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageListValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageList {
    String message() default "";
    Class<?>[] groups() default {};
    int max() default 5;
    Class<? extends Payload>[] payload() default {};
}
