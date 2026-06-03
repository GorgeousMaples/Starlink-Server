package com.app.controller;

import com.app.config.UnityWebSocketHandler;
import com.app.domain.Room;
import com.app.domain.bo.DragBo;
import com.common.core.response.R;
import com.common.core.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final Map<String, Room> roomCache = new ConcurrentHashMap<>();
    private final UnityWebSocketHandler handler;

    @GetMapping("/create")
    public R<String> createRoom(
            @RequestParam String roomId,
            @RequestParam String password
    ) {
        if (roomCache.containsKey(roomId)) {
            return R.error("该房间号已经存在！");
        } else {
            Room room = Room.builder()
                    .id(roomId)
                    .password(password)
                    .host(HeaderUtils.getSessionId())
                    .build();
            roomCache.put(roomId, room);
            System.out.println("新建房间：" + roomId);
            return R.success("房间创建成功！");
        }
    }

    @GetMapping("/getAll")
    public R<List<String>> getAllRooms() {
        List<String> roomIdList = new ArrayList<>(roomCache.keySet());
        return R.success(roomIdList);
    }

    @GetMapping("/enter")
    public R<String> enterRoom(
            @RequestParam String roomId,
            @RequestParam String password
    ) {
        if (!roomCache.containsKey(roomId)) {
            return R.error("该房间号不存在！");
        } else {
            Room room = roomCache.get(roomId);
            if (!password.equals(room.getPassword())) {
                return R.error("房间密码错误");
            }
            String sessionId = HeaderUtils.getSessionId();
            room.setPlayer(sessionId);
            System.out.println(sessionId + " 进入房间 " + roomId);
            return R.success("进入房间成功！");
        }
    }

    @PostMapping("/dragCard")
    public R<String> dragCard(@RequestBody DragBo bo) {
        String msg = String.format("坐标调整为：%d, %d", bo.getI(), bo.getJ());
        String sessionId = HeaderUtils.getSessionId();
        Room room = roomCache.get(HeaderUtils.getRoomId());
        handler.broadcast("MOVE_CARD", bo, room.getAllPlayers(), sessionId);
        return R.success(msg, "通信成功！");
    }
}
