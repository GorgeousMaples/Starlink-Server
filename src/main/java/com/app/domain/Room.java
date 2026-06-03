package com.app.domain;

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
     * 房间密码
     */
    private String password;

    /**
     * 房主 ID
     */
    private String host;

    /**
     * 玩家 ID
     */
    private String player;

    /**
     * 观看者列表
     */
    public List<String> spectators;

    /**
     * 获取房间内所有玩家的会话号
     */
    public List<String> getAllPlayers() {
        List<String> list = new ArrayList<>();
        if (host != null) list.add(host);
        if (player != null) list.add(player);
        if (spectators != null) list.addAll(spectators);
        return list;
    }
}
