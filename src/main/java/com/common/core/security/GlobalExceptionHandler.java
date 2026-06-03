package com.common.core.security;

import cn.dev33.satoken.exception.NotLoginException;
import com.common.core.response.R;

import com.common.core.security.exception.LoginException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 默认错误信息模板
     */
    private String defaultErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        String requestURI = request.getRequestURI();
        return String.format("请求地址: %s, 请求状态: %s, 具体错误信息: %s", requestURI, status, message);
    }

    /**
     * 默认错误响应模板
     */
    private R<Void> defaultErrorResponse(Exception e, HttpServletRequest request, HttpStatus status) {
        String message = defaultErrorMessage(request, status, e.getMessage());
        return R.error(status, message);
    }

    /**
     * 未登录异常处理
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return defaultErrorResponse(e, request, status);
    }

    /**
     * 登录异常处理
     */
    @ExceptionHandler(LoginException.class)
    public R<Void> handleLoginException(LoginException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return defaultErrorResponse(e, request, status);
    }

    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        Map<String, String[]> parameterMap = request.getParameterMap();
        return defaultErrorResponse(e, request, status);
    }
}
