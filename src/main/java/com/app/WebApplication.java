package com.app;

import com.app.service.AccountService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@MapperScan("com.app.mapper")
public class WebApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebApplication.class, args);
//        Map<String, Object> allBeans = context.getBeansOfType(Object.class);
//        for (Map.Entry<String, Object> entry : allBeans.entrySet()) {
//            System.out.println("Bean Name: " + entry.getKey() + ", Bean Class: " + entry.getValue().getClass().getName());
//        }
        AccountService accountService = context.getBean(AccountService.class);
//        accountService.testUpdate();
    }
}