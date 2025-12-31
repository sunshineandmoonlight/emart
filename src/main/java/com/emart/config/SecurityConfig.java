package com.emart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // 禁用 CSRF
            .authorizeHttpRequests()
                // 允许访问的路径
                .antMatchers(
                    "/test/**",              // 测试接口
                    "/swagger-ui/**",        // Swagger UI
                    "/v3/api-docs/**",       // API 文档
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/favicon.ico",
                    "/error",
                    "/user/register",        // 用户注册
                    "/user/login",           // 用户登录
                    "/product/**",           // 商品浏览
                    "/category/**",          // 商品分类
                    "/browse/**",            // 浏览日志
                    "/payment/**"            // 支付接口
                ).permitAll()
                // 其他所有请求都需要认证（暂时也允许访问，等实现用户模块后再改）
                .anyRequest().permitAll();

        return http.build();
    }
}
