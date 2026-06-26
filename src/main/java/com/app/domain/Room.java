package com.app.domain;

import com.app.domain.vo.CardGroupInfoVo;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
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
     * 1号玩家 ID
     */
    private String player1;

    /**
     * 2号玩家 ID
     */
    private String player2;

    /**
     * 1号玩家卡组信息
     */
    private CardGroupInfoVo cardGroup1;

    /**
     * 2 号玩家卡组信息
     */
    private CardGroupInfoVo cardGroup2;

    /**
     * 观看者列表
     */
    public List<String> spectators;

    /**
     * 获取房间内所有玩家的会话号
     */
    public List<String> getAllPlayers() {
        List<String> list = new ArrayList<>();
        if (player1 != null) list.add(player1);
        if (player2 != null) list.add(player2);
        if (spectators != null) list.addAll(spectators);
        return list;
    }
}
