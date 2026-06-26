package com.app.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CardGroupInfoVo {
    /**
     * 卡片列表
     */
    private List<String> cardList;

    /**
     * 卡片ID列表
     */
    private List<Integer> cardIdList;

    /**
     * 角色卡片
     */
    private String roleCard;
}
