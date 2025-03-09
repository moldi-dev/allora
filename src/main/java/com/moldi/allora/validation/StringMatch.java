package com.moldi.allora.validation;

import com.moldi.allora.validation.validator.StringMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringMatchValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringMatch {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String firstFieldName();
    String secondFieldName();
    String errorFieldName();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        StringMatch[] value();
    }
}
