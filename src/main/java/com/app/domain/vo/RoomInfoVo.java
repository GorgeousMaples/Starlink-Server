package com.app.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomInfoVo {
    /**
     * 角色类型【1：玩家1；2：玩家2；3：旁观者】
     */
    private int role;

    /**
     * 角色 ID
     */
    private String sessionId;

    /**
     * 1号玩家卡组信息
     */
    private CardGroupInfoVo cardGroupInfo1;

    /**
     * 2号玩家卡组信息
     */
    private CardGroupInfoVo cardGroupInfo2;
}
