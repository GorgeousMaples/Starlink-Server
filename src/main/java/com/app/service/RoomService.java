package com.app.service;

import com.app.config.UnityWebSocketHandler;
import com.app.domain.Room;
import com.app.domain.User;
import com.app.domain.bo.CardGroupInfoBo;
import com.app.domain.vo.CardGroupInfoVo;
import com.app.domain.vo.RoomInfoVo;
import com.app.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.common.core.response.R;
import com.common.core.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Map<String, Room> roomCache = new ConcurrentHashMap<>();
    private final UnityWebSocketHandler handler;

    /**
     * 创建房间
     */
    public R<RoomInfoVo> createRoom(CardGroupInfoBo bo, String roomId, String password, String sessionId) {
        if (roomCache.containsKey(roomId)) {
            return R.error("该房间号已经存在！");
        }
        Room room = Room.builder()
                .id(roomId)
                .password(password)
                .build();
        CardGroupInfoVo cardGroupVo = buildCardGroupInfo(room, bo);
        room.setPlayer1(sessionId);
        room.setCardGroup1(cardGroupVo);
        roomCache.put(roomId, room);
        System.out.println("新建房间：" + roomId);
        RoomInfoVo roomVo = buildRoomInfo(room, 1, sessionId);
        return R.success(roomVo, "房间创建成功！");
    }

    /**
     * 进入房间
     */
    public R<RoomInfoVo> enterRoom(CardGroupInfoBo bo, String roomId, String password, String sessionId) {
        if (!roomCache.containsKey(roomId)) {
            return R.error("该房间号不存在！");
        }

        Room room = roomCache.get(roomId);
        if (!password.equals(room.getPassword())) {
            return R.error("房间密码错误");
        }

        System.out.println(sessionId + " 进入房间 " + roomId);

        // 1号玩家重新进入房间
        if (sessionId.equals(room.getPlayer1())) {
            RoomInfoVo roomVo = buildRoomInfo(room, 1, sessionId);
            return R.success(roomVo, "1号玩家进入房间");
        }

        // 2号玩家重新进入房间
        if (sessionId.equals(room.getPlayer2())) {
            RoomInfoVo roomVo = buildRoomInfo(room, 2, sessionId);
            return R.success(roomVo, "2号玩家进入房间");
        }

        // 2号玩家首次进入房间
        if (room.getPlayer2() == null) {
            CardGroupInfoVo cardGroupVo = buildCardGroupInfo(room, bo);
            room.setPlayer2(sessionId);
            room.setCardGroup2(cardGroupVo);
            RoomInfoVo roomVo = buildRoomInfo(room, 2, sessionId);
            handler.broadcast("Player2Enter", cardGroupVo, sessionId);
            return R.success(roomVo, "2号玩家首次进入房间");
        }

        // 旁观这进入房间
        RoomInfoVo roomVo = buildRoomInfo(room, 3, sessionId);
        return R.success(roomVo, "旁观者进入房间");
    }

    public R<List<String>> getAllRooms() {
        List<String> roomIdList = new ArrayList<>(roomCache.keySet());
        return R.success(roomIdList);
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
    private RoomInfoVo buildRoomInfo(Room room, int role, String sessionId) {
        RoomInfoVo vo = new RoomInfoVo();
        vo.setRole(role);
        vo.setSessionId(sessionId);
        vo.setCardGroupInfo1(room.getCardGroup1());
        vo.setCardGroupInfo2(room.getCardGroup2());
        return vo;
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
