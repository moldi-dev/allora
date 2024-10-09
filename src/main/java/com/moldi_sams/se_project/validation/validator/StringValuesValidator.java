package com.moldi_sams.se_project.validation.validator;

import com.moldi_sams.se_project.validation.StringValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class StringValuesValidator implements ConstraintValidator<StringValues, String> {

    private List<String> allowedValues;

    @Override
    public void initialize(StringValues stringValues) {
        allowedValues = Arrays.asList(stringValues.allowedValues());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return allowedValues.contains(value);
    }
}
