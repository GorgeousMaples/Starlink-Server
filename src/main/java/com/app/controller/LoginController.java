package com.app.controller;

import com.app.domain.bo.LoginBo;
import com.app.domain.vo.UserAccountListVo;
import com.app.service.LoginService;
import com.common.core.response.R;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Validated
//@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

//    @GetMapping("/getAllAccountByPassword")
    public R<UserAccountListVo> getAllAccountByPassword(@RequestParam @NotBlank String phone, @RequestParam @NotBlank String password) {
        UserAccountListVo accounts = loginService.getAllAccountByPassword(phone, password);
        return R.success(accounts, "查询成功，共查询到 " + accounts.getAccountCount() + " 条数据");
    }

//    @GetMapping("/login")
    public R<String> loginByAccount(@Valid LoginBo bo) {
        String token = loginService.loginByPassword(bo);
        System.out.println(loginService.getCurrentAccount());
        return R.success(token, "登陆成功");
    }

//    @GetMapping("/loginDemo")
    public R<String> loginDemo() {
        LoginBo bo = new LoginBo();
        bo.setAccountId(1899009368760422402L);
        bo.setPhone("12345");
        bo.setPassword("12345");
        String token = loginService.loginByPassword(bo);
        System.out.println(loginService.getCurrentAccount());
        return R.success(token, "登陆成功");
    }
}
