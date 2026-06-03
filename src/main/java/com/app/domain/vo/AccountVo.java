package com.app.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountVo {
    private Long id;

    private Long userId;

    private Integer type;

    /* User类的关联字段 */
    private String name;

    private String password;

    private String phone;

    private String email;
}
