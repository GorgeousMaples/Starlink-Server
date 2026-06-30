package com.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.app.config.UnityWebSocketHandler;
import com.app.domain.Room;
import com.app.domain.bo.CardGroupInfoBo;
import com.app.domain.vo.CardGroupInfoVo;
import com.app.domain.vo.RoomInfoVo;
import com.app.service.RoomService;
import com.common.core.response.R;
import com.common.core.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SaCheckLogin
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public R<RoomInfoVo> createRoom(
            @RequestBody CardGroupInfoBo bo,
            @RequestParam String roomId,
            @RequestParam String password
    ) {
        String uid = HeaderUtils.getPlayerId();
        return roomService.createRoom(bo, roomId, password, uid);
    }

    @PostMapping("/enter")
    public R<RoomInfoVo> enterRoom(
            @RequestBody CardGroupInfoBo bo,
            @RequestParam String roomId,
            @RequestParam String password
    ) {
        String uid = HeaderUtils.getPlayerId();
        return roomService.enterRoom(bo, roomId, password, uid);
    }

    @GetMapping("/getAll")
    public R<List<String>> getAllRooms() {
        return roomService.getAllRooms();
    }
}
