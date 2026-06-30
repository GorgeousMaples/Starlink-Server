package com.app.service;

import cn.dev33.satoken.stp.StpUtil;
import com.app.domain.Player;
import com.app.domain.bo.LoginBo;
import com.app.domain.bo.PlayerBo;
import com.app.domain.vo.PlayerVo;
import com.app.mapper.PlayerMapper;
import com.common.core.response.R;
import com.common.core.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static cn.dev33.satoken.SaManager.log;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerMapper playerMapper;

    private static final int MAX_RETRY = 10; // 最大重试次数

    /**
     * 通过密码登录
     */
    public R<PlayerVo> login(PlayerBo bo) {
        Player player = playerMapper.selectById(bo.getUid());
        if (player == null) {
            return R.error("该账号不存在！");
        }
        if (player.getPassword().equals(bo.getPassword())) {
            StpUtil.login(player.getUid());
            player.setLastLoginTime(LocalDateTime.now()); // 设置登录时间
            playerMapper.updateById(player);
            PlayerVo vo = PlayerVo.builder()
                    .uid(player.getUid())
                    .name(player.getName())
                    .token(StpUtil.getTokenValue())
                    .build();
            return R.success(vo, bo.getName() + "，欢迎回来！");
        } else {
            return R.error("密码错误");
        }
    }

    /**
     * 注册
     */
    public R<PlayerVo> register(PlayerBo bo) {
        if (bo.getPassword().isBlank()) {
            return R.error("密码不能为空");
        }
        if (bo.getName().isBlank()) {
            return R.error("玩家名不能为空");
        }
        Player player = new Player();
        player.setName(bo.getName());
        player.setPassword(bo.getPassword());
        Random random = new Random();
        int retryCount = 0;

        while (retryCount < MAX_RETRY) {
            // 生成 000000 - 999999 的随机数
            int number = random.nextInt(1000000);
            String uid = String.format("%06d", number);

            try {
                // 尝试插入数据库
                player.setUid(uid);
                playerMapper.insert(player);
                PlayerVo vo = PlayerVo.builder()
                        .uid(uid)
                        .name(player.getName())
                        .token(StpUtil.getTokenValue())
                        .build();
                return R.success(vo, "账号注册成功！");
            } catch (DuplicateKeyException e) {
                // 捕获唯一索引冲突异常
                retryCount++;
            }
        }
        return R.error("账号注册失败，请重试");
    }

    /**
     * 修改信息
     */
    public R<Void> modify(PlayerBo bo) {
        try {
            Player player = new Player();
            BeanCopyUtils.copy(bo, player); // 将信息复制给 player
            playerMapper.updateById(player);
            return R.success(null, "玩家信息修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}
