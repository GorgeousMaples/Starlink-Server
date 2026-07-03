package com.app.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoVo {
    /**
     * 角色类型【1：玩家1；2：玩家2；3：旁观者】
     */
    private int role;

    /**
     * 角色 ID
     */
    private String playerId;

    /**
     * 1号玩家卡组信息
     */
    private CardGroupInfoVo cardGroupInfo1;

    /**
     * 2号玩家卡组信息
     */
    private CardGroupInfoVo cardGroupInfo2;
}
