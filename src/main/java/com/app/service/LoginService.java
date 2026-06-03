package com.app.service;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.app.domain.Account;
import com.app.domain.User;
import com.app.domain.bo.LoginBo;
import com.app.domain.vo.AccountVo;
import com.app.domain.vo.UserAccountListVo;
import com.app.mapper.AccountMapper;
import com.app.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.common.core.security.exception.LoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final String accountKey = "account";

    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    public String loginByAccount(AccountVo account) {
        // 将登录用户信息 存入 sa-session（第一级缓存）
        SaStorage storage = SaHolder.getStorage();
        storage.set(accountKey, account);
        // 借助sa-token进行登录
        StpUtil.login(account.getId());
        // 存入第二级缓存
        StpUtil.getSession().set(accountKey, account);
        // 返回 token
        return StpUtil.getTokenValue();
    }

    public String loginByPassword(LoginBo bo) {
        AccountVo accountVo = accountMapper.selectVoById(bo.getAccountId());
        if (accountVo == null) {
            throw new LoginException("错误或未知的账号ID");
        }
        if (accountVo.getPhone().equals(bo.getPhone()) && accountVo.getPassword().equals(bo.getPassword())) {
            return loginByAccount(accountVo);
        } else {
            throw new LoginException("用户名或密码错误");
        }
    }

//    // 根据账户密码获取所有的视图对象
//    public List<AccountVo> getAllAccountVoByPassword(String phone, String password) {
//        return accountMapper.selectVoList(Wrappers.lambdaQuery(User.class)
//                .eq(User::getPhone, phone)
//                .eq(User::getPassword, password)
//        );
//    }

    public UserAccountListVo getAllAccountByPassword(String phone, String password) {
        // 根据手机号、密码筛选出用户
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getPhone, phone)
                .eq(User::getPassword, password)
        );
        if (user == null) {
            throw new LoginException("查询失败，手机号或密码错误");
        }
        // 筛选出所有的账户列表
        List<Account> accountList = accountMapper.selectList(Wrappers.lambdaQuery(Account.class)
                .eq(Account::getUserId, user.getId())
        );
        return UserAccountListVo.builder()
                .userId(user.getId())
                .userName(user.getName())
                .accounts(accountList.stream()
                        .map(account -> UserAccountListVo.SimpleAccountVo.builder()
                                .accountId(account.getId())
                                .type(account.getType())
                                .build())
                        .toList())
                .build();
    }

    public AccountVo getCurrentAccount() {
        // 首先从第一级缓存（storage）中获取
        AccountVo account = (AccountVo) SaHolder.getStorage().get(accountKey);
        if (account != null) {
            return account;
        }
        // 否则尝试从第二级缓存（session）中获取
        SaSession session;
        try {
            session = StpUtil.getTokenSession();
        } catch (Exception e) {
            throw new LoginException("用户未登录或无效的 token");
        }
        if (ObjectUtil.isNull(session)) {
            throw new LoginException("session 意外的为 null");
        }
        account = (AccountVo) session.get(accountKey);
        SaHolder.getStorage().set(accountKey, account);
        return account;
    }
}
