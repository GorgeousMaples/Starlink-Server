package com.common.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求头工具类
 */
public class HeaderUtils {

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("当前非 HTTP 请求上下文");
        }
        return attributes.getRequest();
    }

    /**
     * 获取会话 ID
     */
    public static String getSessionId() {
        return getRequest().getHeader("X-Session-Id");
    }

    /**
     * 获取房间ID
     */
    public static String getRoomId() {
        return getRequest().getHeader("X-Room-Id");
    }
}