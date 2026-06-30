package com.app.controller;

import com.app.domain.bo.PlayerBo;
import com.app.domain.vo.PlayerVo;
import com.app.service.PlayerService;
import com.common.core.response.R;
import com.common.core.utils.HeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/register")
    public R<PlayerVo> register(@RequestBody PlayerBo bo) {
        return playerService.register(bo);
    }

    @PostMapping("/login")
    public R<PlayerVo> login(@RequestBody PlayerBo bo) {
        return playerService.login(bo);
    }

    @PostMapping("/modify")
    public R<Void> modify(@RequestBody PlayerBo bo) {
        return playerService.modify(bo);
    }
}
