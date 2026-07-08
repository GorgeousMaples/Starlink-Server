package com.app.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MyBatisHandler implements MetaObjectHandler {
    // 秒级的时间戳
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
//        String timestamp = LocalDateTime.now().format(FORMATTER);
//        this.strictInsertFill(metaObject, "timestamp", String.class, timestamp);
//         this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        String timestamp = LocalDateTime.now().format(FORMATTER);
//        this.strictUpdateFill(metaObject, "timestamp", String.class, timestamp);
        // this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}