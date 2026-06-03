package com.app.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class LoginBo {
    /**
     * 需要登录的账户ID
     */
    @NotNull
    private Long accountId;

    /**
     * 用户手机号码
     */
    @NotBlank
    private String phone;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;
}
