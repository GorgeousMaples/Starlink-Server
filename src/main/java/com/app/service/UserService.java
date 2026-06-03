package com.app.service;

import com.app.domain.User;
import com.app.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public User getUserByAccount(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return userMapper.selectOne(queryWrapper);
    }

    public List<User> getAllUser() {
        return userMapper.selectList(null);
    }

    public User getUserByPassword(String phone, String password) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getPhone, phone)
                .eq(User::getPassword, password)
        );
    }

    private void throwException() {
        throw new RuntimeException("抛出异常");
    }

    @Transactional
    public void testTransactionInsert() {
        User user = User.builder().id(1L).name("异常前用户").build();
        userMapper.insert(user);
        throwException();
        User user2 = User.builder().id(2L).name("异常后用户").build();
        userMapper.insert(user2);
    }
}
