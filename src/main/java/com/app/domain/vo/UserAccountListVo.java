package com.app.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 用户账户列表视图对象
 *
 * <p>用于表示一个用户的所有账户</p>
 */
@Data
@Builder
public class UserAccountListVo {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户账户列表
     */
    private List<SimpleAccountVo> accounts;

    /**
     * 获取账户数量
     *
     */
    public int getAccountCount() {
        return accounts == null ? 0 : accounts.size();
    }

    /**
     * 简单账户视图对象
     */
    @Data
    @Builder
    public static class SimpleAccountVo {
        /**
         * 账户id
         */
        private Long accountId;

        /**
         * 账户类型
         */
        private Integer type;
    }
}
