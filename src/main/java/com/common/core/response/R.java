package com.common.core.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class R<T> extends ResponseEntity<ResData<T>> {
    private final ResData<T> data = new ResData<>();

    public R(T body, HttpStatusCode status) {
        super(new ResData<>(body, status.value(), ""), status);
    }

    public R(T body, HttpStatusCode status, String message) {
        super(new ResData<>(body, status.value(), message), status);
    }

    public R(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
        data.setCode(status.value());
    }

    public R(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(new ResData<>(body), headers, rawStatus);
        data.setCode(rawStatus);
    }

    public R(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(new ResData<>(body), headers, statusCode);
        data.setCode(statusCode.value());
    }

    public void setMessage(String message) {
        data.setMessage(message);
    }

    public static <T> R<T> build(T data, HttpStatusCode status, String message) {
        return new R<>(data, status, message);
    }

    public static <T> R<T> build(T data, HttpStatusCode status) {
        return new R<>(data, status);
    }

    public static <T> R<T> success(T data) {
        return new R<>(data, HttpStatus.OK);
    }
    public static <T> R<T> success(T data, String message) {
        return new R<>(data, HttpStatus.OK, message);
    }

    public static <T> R<T> notFound(String message) {
        return new R<>(null, HttpStatus.NOT_FOUND, message);
    }

    public static <T> R<T> notFound(T body, String message) {
        return new R<>(body, HttpStatus.NOT_FOUND, message);
    }

    public static <T> R<T> error(String message) {
        return new R<>(null, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> R<T> error(T body, String message) {
        return new R<>(body, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> R<T> error(HttpStatusCode status, String message) {
        return new R<>(null, status, message);
    }
}
