package com.moldi_sams.se_project.validation.validator;

import com.moldi_sams.se_project.validation.StringMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class StringMatchValidator implements ConstraintValidator<StringMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String errorFieldName;

    @Override
    public void initialize(StringMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.firstFieldName();
        this.secondFieldName = constraintAnnotation.secondFieldName();
        this.errorFieldName = constraintAnnotation.errorFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstValue = getFieldValue(value, firstFieldName);
            Object secondValue = getFieldValue(value, secondFieldName);

            boolean isValid = firstValue != null && firstValue.equals(secondValue);

            if (!isValid) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(errorFieldName)
                        .addConstraintViolation();
            }

            return isValid;
        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
