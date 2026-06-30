package com.app.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NotNull
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerVo {
    /**
     * 玩家 ID
     */
    private String uid;

    /**
     * 玩家名
     */
    private String name;

    /**
     * 类别
     */
    private Integer type;

    /**
     * 身份令牌
     */
    private String token;
}
