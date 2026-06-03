package com.app.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BroadcastMessage<T> {
    /**
     * 类型（发送到哪个客户端接口）
     */
    private String type;

    /**
     * 发送的数据
     */
    private T data;
}
