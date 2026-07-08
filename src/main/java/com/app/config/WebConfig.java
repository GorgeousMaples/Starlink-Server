package com.app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 安全构造文件路径（避免手动拼接URI的错误，兼容Windows/Linux）
        String imagePath = fileProperties.getImagePath();
        Path absoluteImagePath = Paths.get(imagePath).toAbsolutePath();
        String resourceLocation = absoluteImagePath.toUri().toString();

        // 2. 自定义资源解析器：防路径遍历+双重校验
        PathResourceResolver secureResolver = new PathResourceResolver() {
            @Override
            protected Resource getResource(String resourcePath, Resource locationResource) throws IOException {
                // 第一层防护：直接拦截包含../的非法路径
                if (resourcePath.contains("..")) {
                    return null; // Spring会自动返回404
                }
                // 解析资源
                Resource resource = super.getResource(resourcePath, locationResource);
                if (resource == null || !resource.exists()) {
                    return null;
                }
                // 第二层防护：校验解析后的真实路径是否在配置的目录内（防符号链接绕过）
                String resourceRealPath = resource.getFile().getCanonicalPath();
                String locationRealPath = locationResource.getFile().getCanonicalPath();
                if (!resourceRealPath.startsWith(locationRealPath)) {
                    return null;
                }
                return resource;
            }
        };

        // 3. 配置静态资源映射+缓存+头处理
        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourceLocation)
                // 缓存策略：1年长期缓存+immutable标记（浏览器无需验证，直接读缓存）
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)
                        .cachePublic()
                        .immutable()
                        .noTransform()) // 禁止CDN/Nginx修改资源
                // 启用资源链
                .resourceChain(true)
                .addResolver(secureResolver);
    }
}