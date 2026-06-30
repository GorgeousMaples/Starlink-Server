package com.app.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class PlayerBo {
    /**
     * 玩家 ID
     */
    private String uid;

    /**
     * 账号密码
     */
    private String password;

    /**
     * 玩家名
     */
    private String name;
}
