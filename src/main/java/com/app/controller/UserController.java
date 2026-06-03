package com.app.controller;

import com.app.domain.User;

import com.app.service.UserService;
import com.common.core.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private <T> R<T> returnData(T data) {
        if(data != null) {
            return R.success(data, "查询成功");
        }
        else {
            return R.notFound("用户不存在");
        }
    }

    @GetMapping("/user")
    public R<User> getUser(@RequestParam("account") String account) {
        User user = userService.getUserByAccount(account);
        if(user != null) {
            return R.success(user, "查询成功");
        }
        else {
            return R.notFound("用户不存在");
        }
    }

    @GetMapping("/hello/{name}")
    public String getNameByPath(@PathVariable("name") String name) {
        return "<h1>" + "你好，" + name + "! </h1>";
    }

    @GetMapping("/users")
    public R<List<User>> getAllUser() {
        List<User> users = userService.getAllUser();
//        if(users != null) {
//            return R.success(users, "查询成功");
//        }
//        else {
//            return R.notFound("用户不存在");
//        }
        return returnData(users);
    }
}
