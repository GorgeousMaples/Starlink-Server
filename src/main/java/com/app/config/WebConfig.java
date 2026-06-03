package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将访问 /images/** 的请求映射到服务器的对应目录下
        String location = "file:///" + fileProperties.getImagePath();

        registry.addResourceHandler("/images/**")
                .addResourceLocations(location);
    }
}