package com.common.core.utils;

import com.app.domain.vo.CardGroupInfoVo;
import com.app.domain.vo.CardInfoVo;

import java.util.List;

/**
 * 卡片操作工具类
 * 一个 Room 中一个
 */
public class CardHandler {
    /**
     * 己方卡组
     */
    private CardGroupInfoVo selfGroup;

    /**
     * 对方卡组
     */
    private CardGroupInfoVo otherGroup;

    /**
     * 绑定卡组
     */
    public void bind(CardGroupInfoVo selfGroup, CardGroupInfoVo otherGroup) {
        this.selfGroup = selfGroup;
        this.otherGroup = otherGroup;
    }

    /**
     * 获取手牌区列表
     */
    public List<Integer> getHandList(boolean isOpponent) {
        return isOpponent ? otherGroup.getHandList() : selfGroup.getHandList();
    }

    /**
     * 获取卡区的列表
     */
    public List<Integer> getAreaList(int areaId) {
        return switch (areaId) {
            case 0 -> selfGroup.getCardAreaList();
            case 1 -> selfGroup.getFoldAreaList();
            case 2 -> selfGroup.getExileAreaList();
            case 3 -> otherGroup.getCardAreaList();
            case 4 -> otherGroup.getFoldAreaList();
            case 5 -> otherGroup.getExileAreaList();
            default -> throw new RuntimeException("错误的卡区编号: " + areaId);
        };
    }

    /**
     * 移除指定卡区的卡片
     */
    public void removeAreaCard(int areaId, Integer cardId) {
        List<Integer> list = getAreaList(areaId);
        if (!list.remove(cardId)) {
            throw new RuntimeException(areaId + "号卡区中没有该卡牌: " + cardId);
        }
    }

    /**
     * 从手牌或者场上移除卡片
     * @return 是自己的手牌还是对方的手牌
     */
    public boolean removeCard(Integer key) {
        if (selfGroup.frameMap.containsKey(key)) {
            selfGroup.frameMap.remove(key);
            return true;
        } else if (selfGroup.getHandList().remove(key)) {
            return true;
        } else if (otherGroup.frameMap.containsKey(key)) {
            otherGroup.frameMap.remove(key);
            return false;
        } else if (otherGroup.getHandList().remove(key)) {
            return false;
        } else {
            throw new RuntimeException("场上或手牌中没有该卡牌: " + key);
        }
    }

    /**
     * 设置卡片位置
     */
    public void setCardPosition(boolean isSelf, int key, int pos) {
        if (isSelf) {
            CardInfoVo card = new CardInfoVo(key, pos);
            selfGroup.frameMap.put(key, card);
        } else {
            CardInfoVo card = new CardInfoVo(key, 29 - pos);
            otherGroup.frameMap.put(key, card);
        }
    }

    /**
     * 从场上获取手牌（不包含手牌）
     */
    public CardInfoVo getCard(Integer key) {
        if (selfGroup.frameMap.containsKey(key)) {
            return selfGroup.frameMap.get(key);
        } else if (otherGroup.frameMap.containsKey(key)) {
            return otherGroup.frameMap.get(key);
        } else {
            throw new RuntimeException("场上没有该卡牌: " + key);
        }
    }
}
