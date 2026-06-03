package com.app.service;

import com.app.domain.Account;
import com.app.domain.User;
import com.app.domain.bo.LoginBo;
import com.app.domain.vo.AccountVo;
import com.app.mapper.AccountMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.common.core.security.exception.LoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;

    private final UserService userService;

    public AccountVo getAccountVoById(Long id) {
        return accountMapper.selectVoById(id);
    }

    public List<Account> getAllAccountByPassword(String phone, String password) {
        User user = userService.getUserByPassword(phone, password);
        if (user == null) {
            throw new LoginException("用户名或密码错误");
        }
        return accountMapper.selectList(Wrappers.<Account>lambdaQuery()
                .eq(Account::getUserId, user.getId())
        );
    }

//    public void testUpdate() {
//        Account account = new Account();
//        account.setId(1899015618407653377L);
//        account.setType(3);
//        accountMapper.updateById(account);
//    }
}
