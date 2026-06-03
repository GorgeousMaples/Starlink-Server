package com.app.mapper;

import com.app.domain.Account;
import com.app.domain.vo.AccountVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.common.mybatis.mapper.BaseMapperPlus;
import com.common.mybatis.wrapper.AutoQueryWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@AutoQueryWrapper
public interface AccountMapper extends BaseMapperPlus<AccountMapper, Account, AccountVo> {
    /**
     * 根据条件查询账户视图对象列表
     */
    List<AccountVo> selectVoList(@Param(Constants.WRAPPER) Wrapper<?> queryWrapper);

    /**
     * 根据账户ID查询账户视图对象
     */
    AccountVo selectVoById(Long id);
}
