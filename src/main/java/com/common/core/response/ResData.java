package com.common.core.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResData<T>  {
    private int code = 200;
    private T data = null;
    private String message = "";

    public ResData(T data) {
        this.data = data;
    }

    public ResData(T data, int code, String message) {
        this.data = data;
        this.message = message;
        this.code = code;
    }
}