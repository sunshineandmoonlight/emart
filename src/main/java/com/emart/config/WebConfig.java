package com.emart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源映射
     * 将 /uploads/** 和 /images/** 映射到本地磁盘
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows系统
        String uploadsPath = "file:D:/test/project/emart/uploads/";
        String imagesPath = "file:D:/test/project/emart/images/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsPath);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesPath);
    }
}
