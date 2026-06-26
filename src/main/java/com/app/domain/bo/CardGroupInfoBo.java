package com.app.domain.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CardGroupInfoBo implements Serializable {
    // 卡片列表
    private List<String> cardList;

    // 角色卡片
    private String roleCard;
}
