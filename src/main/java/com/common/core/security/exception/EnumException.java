package com.common.core.security.exception;

public class EnumException extends RuntimeException {

    public EnumException(Class<? extends Enum<?>> enumType, int code) {
        super(enumType.getName() + " 中不存在 code: " + code);
    }
}
