package com.app.mapper;

import com.app.domain.Account;
import com.app.domain.vo.AccountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TestMapper extends BaseMapper<Account> {
    @Select(""" 
    select
        t_account.*,
        t_user.C_NAME, t_user.C_PASSWORD, t_user.C_PHONE, t_user.C_EMAIL
    from t_account inner join t_user
        on t_account.C_USER_ID = t_user.C_ID
    """)
    @Results(id = "AccountVoResultMap", value = {
            @Result(column = "C_ID", property = "id"),
            @Result(column = "C_USER_ID", property = "userId"),
            @Result(column = "C_TYPE", property = "type"),
            @Result(column = "C_NAME", property = "name"),
            @Result(column = "C_PASSWORD", property = "password"),
            @Result(column = "C_PHONE", property = "phone"),
            @Result(column = "C_EMAIL", property = "email")
    })
    List<AccountVo> selectVoList();

    @Select(""" 
    select
        t_account.*,
        t_user.C_NAME, t_user.C_PASSWORD, t_user.C_PHONE, t_user.C_EMAIL
    from t_account inner join t_user
        on t_account.C_USER_ID = t_user.C_ID
    where t_account.C_ID = #{id}
    """)
    @ResultMap("AccountVoResultMap")
    AccountVo selectVoById(Long id);
}
