package com.common.core.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntegerConstraintValidator.class)
public @interface IntegerConstraint {
    int[] values() default {};
    boolean notNull() default false;
    String message() default "不符合规定的值";
    // 分组支持
    Class<?>[] groups() default {};
    // 元数据传递
    Class<? extends Payload>[] payload() default {};
}
