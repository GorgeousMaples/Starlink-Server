package com.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.core.utils.WrapperUtils;
import com.common.mybatis.wrapper.WhereItem;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WrapperController {
    @PostMapping("queryWrapper")
    public String getSQL(@RequestBody WhereItem[] items) {
        QueryWrapper<?> wrapper = new QueryWrapper<>();
        return WrapperUtils.getWhereSentence(WhereItem.buildQueryWrapper(wrapper, items));
    }
}
