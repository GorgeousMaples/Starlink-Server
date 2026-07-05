package com.app.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 卡片信息类
 * 用于表示出战的卡片信息
 */
@Data
public class CardInfoVo {
    /**
     * 卡片 key
     */
    private Integer key;

    /**
     * 卡框编号
     */
    private Integer pos;

    /**
     * 贴纸
     */
    public int[] stickers = new int[]{-1, -1, -1, -1};

    public CardInfoVo(int key) {
        this.key = key;
    }
}
