package com.app.domain;

import com.app.domain.bo.CardGroupInfoBo;
import com.app.domain.vo.CardGroupInfoVo;
import com.common.core.utils.CardHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    /**
     * 房间 ID
     */
    private String id;

    /**
     * 卡片 ID（会不断自增）
     */
    public int cardId = 0;

    /**
     * 房间密码
     */
    private String password;

    /**
     * 1号玩家
     */
    public PlayerInfo player1;

    /**
     * 2号玩家 ID
     */
    public PlayerInfo player2;

    /**
     * 观看者列表
     */
    public Set<String> spectators;

    /**
     * 卡片操作工具
     */
    @JsonIgnore
    public CardHandler handler;

    /**
     * 构造方法
     */
    public Room(String id, String password) {
        this.id = id;
        this.password = password;
        player1 = new PlayerInfo();
        player2 = new PlayerInfo();
        spectators = new HashSet<>();
        handler = new CardHandler();
    }

    /**
     * 玩家信息内部类
     */
    public static class PlayerInfo {
        /**
         * 玩家 ID
         */
        public String id;

        /**
         * 卡组信息
         */
        public CardGroupInfoVo cardGroup = new CardGroupInfoVo();

        /**
         * 玩家是否还在房间内
         */
        public boolean isInRoom = false;

        /**
         * 绑定玩家信息
         */
        public void bindInfo(String playerId) {
            id = playerId;
            isInRoom = true;
        }

        public CardGroupInfoVo getCardGroupInfo() {
            if (cardGroup == null) return null;
            return cardGroup.convert();
        }
    }

    /**
     * 获取房间内所有玩家的ID
     */
    public List<String> getAllPlayers() {
        List<String> list = new ArrayList<>();
        if (player1 != null && player1.isInRoom) list.add(player1.id);
        if (player2 != null && player2.isInRoom) list.add(player2.id);
        if (spectators != null) list.addAll(spectators);
        return list;
    }

    /**
     * 初始化卡组信息
     */
    public void initCardGroup(boolean isPlayer1, CardGroupInfoBo bo, String playerName) {
        CardGroupInfoVo vo = isPlayer1 ? player1.cardGroup : player2.cardGroup;
        vo.setPlayerName(playerName);
        List<Integer> keyList = vo.getKeyList();
        for (int i = 0; i < bo.getCardList().size(); i++) {
            keyList.add(cardId++);
        }
        vo.setCardList(bo.getCardList());
        List<Integer> cardAreaList = new ArrayList<>(keyList);
        Collections.shuffle(cardAreaList); // 打乱顺序
        vo.getCardAreaList().addAll(cardAreaList); // 全都添加进去
    }
}
