package com.common.core.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerConstraintValidator implements ConstraintValidator<IntegerConstraint, Integer> {
    private List<Integer> values;
    private boolean notNull;
    private String message;

    @Override
    public void initialize(IntegerConstraint constraintAnnotation) {
        values = Arrays.stream(constraintAnnotation.values()).boxed().collect(Collectors.toList());
        notNull = constraintAnnotation.notNull();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer == null) {
            if(notNull) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            } else {
                return true;
            }
        } else {
            if(values.contains(integer)) {
                return true;
            } else {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
        }
    }
}
