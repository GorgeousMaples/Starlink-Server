package com.app.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseBo {
    /**
     * 客户端的会话ID
     */
    private String sessionId;
}
