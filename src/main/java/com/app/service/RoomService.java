package com.app.service;

import com.app.config.UnityWebSocketHandler;
import com.app.domain.Player;
import com.app.domain.Room;
import com.app.domain.bo.CardGroupInfoBo;
import com.app.domain.vo.CardGroupInfoVo;
import com.app.domain.vo.RoomInfoVo;
import com.app.mapper.PlayerMapper;
import com.common.core.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private final Map<String, Room> roomCache = new ConcurrentHashMap<>(); // key: roomId
    private final Map<String, Room> playerRoomCache = new ConcurrentHashMap<>(); // key: playerId

    @Autowired
    private UnityWebSocketHandler handler;

    @Autowired
    private PlayerMapper playerMapper;

    /**
     * 创建房间
     */
    public R<RoomInfoVo> createRoom(CardGroupInfoBo bo, String roomId, String password, String playerId) {
        if (roomCache.containsKey(roomId)) {
            return R.error("该房间号已经存在！");
        }
        Room room = Room.builder()
                .id(roomId)
                .password(password)
                .spectators(new HashSet<>())
                .build();
        CardGroupInfoVo cardGroupVo = buildCardGroupInfo(room, bo);
        room.setPlayer1(playerId);
        room.setCardGroup1(cardGroupVo);
        roomCache.put(roomId, room);
        playerRoomCache.put(playerId, room);
        System.out.println("新建房间：" + roomId);
        RoomInfoVo roomVo = buildRoomInfo(room, 1, playerId);
        return R.success(roomVo, "房间创建成功！");
    }

    /**
     * 进入房间
     */
    public R<RoomInfoVo> enterRoom(CardGroupInfoBo bo, String roomId, String password, String playerId) {
        if (!roomCache.containsKey(roomId)) {
            return R.error("该房间号不存在！");
        }

        Room room = roomCache.get(roomId);
        if (!password.equals(room.getPassword())) {
            return R.error("房间密码错误");
        }

        System.out.println(playerId + " 进入房间 " + roomId);
        playerRoomCache.put(playerId, room);

        // 1号玩家重新进入房间
        if (playerId.equals(room.getPlayer1())) {
            RoomInfoVo roomVo = buildRoomInfo(room, 1, playerId);
            return R.success(roomVo, "1号玩家进入房间");
        }

        // 2号玩家重新进入房间
        if (playerId.equals(room.getPlayer2())) {
            RoomInfoVo roomVo = buildRoomInfo(room, 2, playerId);
            return R.success(roomVo, "2号玩家进入房间");
        }

        // 2号玩家首次进入房间
        if (room.getPlayer2() == null) {
            CardGroupInfoVo cardGroupVo = buildCardGroupInfo(room, bo);
            room.setPlayer2(playerId);
            room.setCardGroup2(cardGroupVo);
            RoomInfoVo roomVo = buildRoomInfo(room, 2, playerId);
            handler.broadcast("Player2Enter", cardGroupVo, room, playerId);
            return R.success(roomVo, "2号玩家首次进入房间");
        }

        // 旁观者进入房间
        RoomInfoVo roomVo = buildRoomInfo(room, 3, playerId);
        room.spectators.add(playerId);
        return R.success(roomVo, "旁观者进入房间");
    }

    /**
     * 玩家离开房间
     */
    public R<String> leaveRoom(String playerId) {
        Room room = playerRoomCache.get(playerId);
        if (room == null) {
            return R.error("房间为空");
        }
        Player player = playerMapper.selectById(playerId);
        String name = player.getName();
        String message = null;
        if (room.spectators.contains(playerId)) {
            room.spectators.remove(playerId);
            message = String.format("旁观者【%s】离开房间", name);
        } else if (room.getPlayer1().equals(playerId)) {
            room.setPlayer1(null);
            message = String.format("1号玩家【%s】离开房间", name);
        } else if (room.getPlayer2().equals(playerId)) {
            room.setPlayer2(null);
            message = String.format("2号玩家【%s】离开房间", name);
        }
        // 两个玩家都离开房间，则清除房间
        if (room.getPlayer1() == null && room.getPlayer2() == null) {
            roomCache.remove(room.getId());
            message = message + "，房间已关闭";
        }
        handler.broadcast("ShowMessage", message, room, playerId); // 广播
        playerRoomCache.remove(playerId); // 清除缓存
        return R.success(message);
    }

    /**
     * 获取所有房间列表
     */
    public R<List<String>> getAllRooms() {
        List<String> roomIdList = new ArrayList<>(roomCache.keySet());
        return R.success(roomIdList);
    }

    /**
     * 向指定房间进行广播
     */
    public R<String> broadcastRoom(String url, Map<String, Object> map, String playerId) {
        Room room = playerRoomCache.get(playerId);
        handler.broadcast(url, map, room, playerId); // 广播
        return R.success(map.toString(), url);
    }

    /**
     * 构建卡组信息
     */
    private CardGroupInfoVo buildCardGroupInfo(Room room, CardGroupInfoBo bo) {
        CardGroupInfoVo vo = new CardGroupInfoVo();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < bo.getCardList().size(); i++) {
            ids.add(room.cardId++);
        }
        vo.setCardList(bo.getCardList());
        vo.setCardIdList(ids);
        return vo;
    }

    /**
     * 构建房间信息
     */
    private RoomInfoVo buildRoomInfo(Room room, int role, String playerId) {
        return RoomInfoVo.builder()
                .role(role)
                .playerId(playerId)
                .cardGroupInfo1(room.getCardGroup1())
                .cardGroupInfo2(room.getCardGroup2())
                .build();
    }

//    /**
//     * 清理空房间（可选，用于定时任务）
//     */
//    @Scheduled(fixedDelay = 300000) // 每5分钟执行一次
//    public void cleanupEmptyRooms() {
//        roomCache.entrySet().removeIf(entry ->
//                entry.getValue().getPlayer1() == null &&
//                        entry.getValue().getPlayer2() == null
//        );
//    }
}
