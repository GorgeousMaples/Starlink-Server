package com.app.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public class CardGroupInfoVo {
    /**
     * 玩家名
     */
    private String playerName;

    /**
     * 卡片 ID 列表
     */
    private List<String> cardList = new ArrayList<>();

    /**
     * 卡片 key 列表
     */
    private List<Integer> keyList = new ArrayList<>();

    /**
     * 角色卡片
     */
    private String roleCard;

    /**
     * 手牌区列表
     */
    private List<Integer> handList = new ArrayList<>();

    /**
     * 卡组区列表
     */
    private List<Integer> cardAreaList = new ArrayList<>();

    /**
     * 弃牌区列表
     */
    private List<Integer> foldAreaList = new ArrayList<>();

    /**
     * 流放区列表
     */
    private List<Integer> exileAreaList = new ArrayList<>();

    /**
     * 卡框列表
     */
    private List<CardInfoVo> frameList = new ArrayList<>();

    /**
     * 星能值
     */
    private Integer starValue = 0;

    /**
     * 核心值1
     */
    private Integer coreValue1 = 20;

    /**
     * 核心值2
     */
    private Integer coreValue2 = 20;

    /**
     * 卡框字典（不参与序列化，用于转 frameList）
     */
    @JsonIgnore
    public Map<Integer, CardInfoVo> frameMap = new ConcurrentHashMap<>();

    /**
     * 将原始类转换成可 JSON 化的类
     */
    public CardGroupInfoVo convert() {
        frameList = new ArrayList<>(frameMap.values());
        return this;
    }
}
