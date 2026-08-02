package com.app.service;

import com.app.config.UnityWebSocketHandler;
import com.app.domain.Player;
import com.app.domain.Room;
import com.app.domain.bo.CardGroupInfoBo;
import com.app.domain.vo.CardGroupInfoVo;
import com.app.domain.vo.RoomInfoVo;
import com.app.mapper.PlayerMapper;
import com.common.core.response.R;
import com.common.core.utils.CardHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private final Map<String, Room> roomCache = new ConcurrentHashMap<>(); // key: roomId
    private final Map<String, Room> playerRoomCache = new ConcurrentHashMap<>(); // key: playerId

    @Autowired
    private UnityWebSocketHandler handler;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private GameService gameService;

    /**
     * 创建房间
     */
    public R<RoomInfoVo> createRoom(CardGroupInfoBo bo, String roomId, String password, String playerId) {
        if (roomCache.containsKey(roomId)) {
            return R.error("该房间号已经存在！");
        }
        Room room = new Room(roomId, password);
        Player player = playerMapper.selectById(playerId);
        String name = player.getName();
        room.initCardGroup(true, bo, name);
        room.player1.bindInfo(playerId);
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
        Player player = playerMapper.selectById(playerId);
        String name = player.getName();
        if (!password.equals(room.getPassword())) {
            return R.error("房间密码错误");
        }

        System.out.println(playerId + " 进入房间 " + roomId);
        playerRoomCache.put(playerId, room);

        // 1号玩家重新进入房间
        if (playerId.equals(room.player1.id)) {
            RoomInfoVo roomVo = buildRoomInfo(room, 1, playerId);
            room.player1.isInRoom = true;
            return R.success(roomVo, "1号玩家回到房间");
        }

        // 2号玩家重新进入房间
        if (playerId.equals(room.player2.id)) {
            RoomInfoVo roomVo = buildRoomInfo(room, 2, playerId);
            room.player2.isInRoom = true;
            return R.success(roomVo, "2号玩家回到房间");
        }

        // 2号玩家首次进入房间
        if (room.getPlayer2().id == null) {
            room.initCardGroup(false, bo, name);
            room.player2.bindInfo(playerId);
            RoomInfoVo roomVo = buildRoomInfo(room, 2, playerId);
            handler.broadcast("Player2Enter", room.player2.cardGroup, room, playerId);
            return R.success(roomVo, String.format("2号玩家【%s】加入房间", name));
        }

        // 旁观者进入房间
        RoomInfoVo roomVo = buildRoomInfo(room, 3, playerId);
        room.spectators.add(playerId);
        return R.success(roomVo,  String.format("旁观者【%s】加入房间", name));
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
        } else if (room.player1.id.equals(playerId)) {
            room.player1.isInRoom = false;
            message = String.format("1号玩家【%s】离开房间", name);
        } else if (room.player2.id.equals(playerId)) {
            room.player2.isInRoom = false;
            message = String.format("2号玩家【%s】离开房间", name);
        }
        // 两个玩家都离开房间，则清除房间
        if (!room.player1.isInRoom && !room.player2.isInRoom) {
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
        if (room.player1.id.equals(playerId)) {
            room.handler.bind(room.player1.cardGroup, room.player2.cardGroup);
//            gameService.invokeMethod(url, map, room.player1.cardGroup, room.player2.cardGroup);
        } else if (room.player2.id.equals(playerId)) {
            room.handler.bind(room.player2.cardGroup, room.player1.cardGroup);
//            gameService.invokeMethod(url, map, room.player2.cardGroup, room.player1.cardGroup);
        } else {
            return R.error(String.format("异常的玩家ID，%s既不是1号玩家也不是2号玩家", playerId));
        }
        System.out.println("[" + url + "]：" + map.toString());
        gameService.invokeMethod(url, map, room.handler);
        handler.broadcast(url, map, room, playerId); // 广播
        return R.success(map.toString(), url);
    }

//    /**
//     * 初始化卡组信息
//     */
//    private void initCardGroupInfo(Room room, CardGroupInfoVo vo, CardGroupInfoBo bo, String playerId) {
//        Player player = playerMapper.selectById(playerId);
//        vo.setPlayerName(player.getName());
//        List<Integer> keyList = vo.getKeyList();
//        for (int i = 0; i < bo.getCardList().size(); i++) {
//            keyList.add(room.cardId++);
//        }
//        vo.setCardList(bo.getCardList());
//        List<Integer> cardAreaList = new ArrayList<>(keyList);
//        Collections.shuffle(cardAreaList); // 打乱顺序
//        vo.getCardAreaList().addAll(cardAreaList); // 全都添加进去
//    }

    /**
     * 构建房间信息
     */
    private RoomInfoVo buildRoomInfo(Room room, int role, String playerId) {
        return RoomInfoVo.builder()
                .role(role)
                .playerId(playerId)
                .cardGroupInfo1(room.player1.getCardGroupInfo())
                .cardGroupInfo2(room.player2.getCardGroupInfo())
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
