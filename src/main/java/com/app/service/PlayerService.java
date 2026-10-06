package com.app.service;

import cn.dev33.satoken.stp.StpUtil;
import com.app.config.UnityWebSocketHandler;
import com.app.domain.Player;
import com.app.domain.bo.PlayerBo;
import com.app.domain.vo.PlayerVo;
import com.app.domain.vo.PlayerSummaryVo;
import com.app.mapper.PlayerMapper;
import com.common.core.response.R;
import com.common.core.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerMapper playerMapper;
    private final UnityWebSocketHandler handler;
    private final ObjectMapper objectMapper;

    private static final int MAX_RETRY = 10; // 最大重试次数

    /** 管理端只读账户列表。 */
    public R<List<PlayerSummaryVo>> getAccounts() {
        Player current = playerMapper.selectById(String.valueOf(StpUtil.getLoginId()));
        if (current == null || (!Integer.valueOf(1).equals(current.getType())
                && !Integer.valueOf(2).equals(current.getType()))) {
            return R.error(HttpStatus.FORBIDDEN, "仅管理员可以查看账户列表");
        }

        List<PlayerSummaryVo> accounts = playerMapper.selectList(
                        Wrappers.<Player>lambdaQuery()
                                .select(Player::getUid, Player::getType, Player::getName,
                                        Player::getCreateTime, Player::getLastLoginTime)
                                .orderByAsc(Player::getUid))
                .stream()
                .map(player -> new PlayerSummaryVo(player.getUid(), player.getType(), player.getName(),
                        player.getCreateTime(), player.getLastLoginTime()))
                .toList();
        return R.success(accounts);
    }

    /**
     * 通过密码登录
     */
    public R<PlayerVo> login(PlayerBo bo, String sessionId) {
        Player player = playerMapper.selectById(bo.getUid());
        if (player == null) {
            return R.error("该账号不存在！");
        }
        if (player.getPassword().equals(bo.getPassword())) {
            StpUtil.login(player.getUid());
            player.setLastLoginTime(LocalDateTime.now()); // 设置登录时间
            playerMapper.updateById(player);
            PlayerVo vo = PlayerVo.builder()
                    .token(StpUtil.getTokenValue())
                    .build();
            BeanCopyUtils.copy(player, vo);
            handler.bindSession(player.getUid(), sessionId);
            return R.success(vo, bo.getName() + "，欢迎回来！");
        } else {
            return R.error("密码错误");
        }
    }

    /**
     * 注册
     */
    public R<PlayerVo> register(PlayerBo bo, String sessionId) {
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
                player.setLastLoginTime(LocalDateTime.now());
                playerMapper.insert(player);
                StpUtil.login(uid);
                PlayerVo vo = PlayerVo.builder()
                        .token(StpUtil.getTokenValue())
                        .build();
                BeanCopyUtils.copy(player, vo);
                handler.bindSession(uid, sessionId);
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
        if (bo.getCardGroup() != null) {
            try {
                JsonNode groups = objectMapper.readTree(bo.getCardGroup());
                JsonNode list = groups == null ? null : groups.get("group");
                JsonNode index = groups == null ? null : groups.get("currentIndex");
                if (list == null || !list.isArray() || list.isEmpty() || index == null ||
                        !index.isInt() || index.intValue() < 0 || index.intValue() >= list.size()) {
                    return R.error(HttpStatus.BAD_REQUEST, "卡组数据无效");
                }
                for (JsonNode group : list) {
                    if (!group.hasNonNull("name") || !group.path("cardIds").isArray() ||
                            !group.path("cardCollIds").isArray()) {
                        return R.error(HttpStatus.BAD_REQUEST, "卡组数据无效");
                    }
                }
            } catch (JsonProcessingException e) {
                return R.error(HttpStatus.BAD_REQUEST, "卡组 JSON 无效");
            }
        }
        try {
            Player player = new Player();
            player.setUid((String) StpUtil.getLoginId());
            if (bo.getName() != null && !bo.getName().isBlank()) player.setName(bo.getName());
            if (bo.getPassword() != null && !bo.getPassword().isBlank()) player.setPassword(bo.getPassword());
            // 客户端提交完整卡组列表；删除操作通过覆盖旧 JSON 生效。
            if (bo.getCardGroup() != null) player.setCardGroup(bo.getCardGroup());
            if (playerMapper.updateById(player) != 1) return R.notFound("玩家不存在");
            return R.success(null, "玩家信息修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}
