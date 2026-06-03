package com.app.mapper;

import com.app.domain.AccountMessage;
import com.app.domain.vo.AccountMessageVo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.mybatis.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccountMessageMapper extends BaseMapperPlus<AccountMessageMapper, AccountMessage, AccountMessageVo> {
    @Select("""
    select
        t_account_message.*,
        t_message.C_TITLE,
        t_message.C_CONTENT
    from t_account_message
        left join t_message on C_MESSAGE_ID = t_message.C_ID
    where C_ACCOUNT_ID = #{accountId}
    """)
    @Results(id = "AccountMessageVoResultMap", value = {
            @Result(column = "C_ID", property = "id"),
            @Result(column = "C_ACCOUNT_ID", property = "accountId"),
            @Result(column = "C_MESSAGE_ID", property = "messageId"),
            @Result(column = "C_TYPE", property = "messageType"),
            @Result(column = "C_TITLE", property = "title"),
            @Result(column = "C_CONTENT", property = "content")
    })
    List<AccountMessageVo> selectVoListByAccountId(Long accountId);

    @Select("""
    select
        t_account_message.*,
        t_message.C_TITLE,
        t_message.C_CONTENT
    from t_account_message
        inner join t_message on C_MESSAGE_ID = t_message.C_ID
        inner join t_account on C_ACCOUNT_ID = t_account.C_ID
    """)
    @ResultMap("AccountMessageVoResultMap")
    List<AccountMessageVo> selectVoList(Page<AccountMessage> page);
}
