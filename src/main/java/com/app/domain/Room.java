package com.app.domain;

import com.app.domain.vo.CardGroupInfoVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

//    /**
//     * 1号玩家 ID
//     */
//    private String player1;
//
//    /**
//     * 2号玩家 ID
//     */
//    private String player2;
//
//    /**
//     * 1号玩家卡组信息
//     */
//    private CardGroupInfoVo cardGroup1;
//
//    /**
//     * 2 号玩家卡组信息
//     */
//    private CardGroupInfoVo cardGroup2;
//
//    /**
//     * 1 号玩家是否在游戏中
//     */
//    private boolean isPlay1 = false;
//
//    /**
//     * 2 号玩家是否在游戏中
//     */
//    private boolean isPlay2 = false;

    /**
     * 观看者列表
     */
    public Set<String> spectators = new HashSet<>();

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
        public CardGroupInfoVo cardGroup;

        /**
         * 玩家是否还在房间内
         */
        public boolean isInRoom = false;

        /**
         * 绑定玩家信息
         */
        public void bindInfo(String playerId, CardGroupInfoVo cardGroupInfo) {
            id = playerId;
            cardGroup = cardGroupInfo;
            isInRoom = true;
        }
    }

    /**
     * 获取房间内所有玩家的会话号
     */
    public List<String> getAllPlayers() {
        List<String> list = new ArrayList<>();
        if (player1 != null && player1.isInRoom) list.add(player1.id);
        if (player2 != null && player2.isInRoom) list.add(player2.id);
        if (spectators != null) list.addAll(spectators);
        return list;
    }
}
