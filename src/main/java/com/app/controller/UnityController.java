package com.app.controller;

import com.app.config.UnityWebSocketHandler;
import com.app.domain.bo.DragBo;
import com.app.domain.bo.LoginBo;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.common.core.response.R;
import com.common.core.utils.HeaderUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UnityController {
//    private final IdentifierGenerator idGenerator;

    private final UnityWebSocketHandler handler;

//    @GetMapping("/id")
//    public R<Long> generateId() {
//        Long id = idGenerator.nextId(null).longValue();
//        return R.success(id, "成功生成id");
//    }

//    @GetMapping("/unity")
//    public R<String> testUnity() {
//        return R.success("你好，Unity", "通信成功！");
//    }
//
//    @PostMapping("/dragCard")
//    public R<String> dragCard(@RequestBody DragBo bo) {
//        String msg = String.format("坐标调整为：%d, %d", bo.getI(), bo.getJ());
//        String sessionId = HeaderUtils.getSessionId();
//        handler.broadcast("MOVE_CARD", bo, sessionId);
//        return R.success(msg, "通信成功！");
//    }
}
