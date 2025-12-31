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
        // 使用动态路径，获取项目根目录
        String projectRoot = System.getProperty("user.dir");
        String uploadsPath = "file:" + projectRoot + java.io.File.separator + "uploads" + java.io.File.separator;
        String imagesPath = "file:" + projectRoot + java.io.File.separator + "images" + java.io.File.separator;

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsPath);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesPath);
    }
}
