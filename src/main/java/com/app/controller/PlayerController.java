package com.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.app.domain.bo.PlayerBo;
import com.app.domain.vo.PlayerVo;
import com.app.domain.vo.PlayerSummaryVo;
import com.app.service.PlayerService;
import com.common.core.response.R;
import com.common.core.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/register")
    public R<PlayerVo> register(@RequestBody PlayerBo bo) {
        String sessionId = HeaderUtils.getSessionId();
        return playerService.register(bo, sessionId);
    }

    @PostMapping("/login")
    public R<PlayerVo> login(@RequestBody PlayerBo bo) {
        String sessionId = HeaderUtils.getSessionId();
        return playerService.login(bo, sessionId);
    }

    @GetMapping("/player/accounts")
    @SaCheckLogin
    public R<List<PlayerSummaryVo>> getAccounts() {
        return playerService.getAccounts();
    }

    @PostMapping("/modify")
    @SaCheckLogin
    public R<Void> modify(@RequestBody PlayerBo bo) {
        return playerService.modify(bo);
    }
}
