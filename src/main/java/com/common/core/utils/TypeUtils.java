package com.common.core.utils;

public class TypeUtils extends com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils {
    public static Class<?> resolveTypeArgument(final Class<?> clazz, final Class<?> genericInterface) {
        return resolveTypeArguments(clazz, genericInterface)[0];
    }
}
