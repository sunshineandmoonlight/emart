package com.emart.controller;

import com.emart.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@Tag(name = "测试接口", description = "系统测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public CommonResult<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("message", "Emart API is running");
        data.put("timestamp", System.currentTimeMillis());
        return CommonResult.success(data);
    }

    @Operation(summary = "欢迎信息")
    @GetMapping("/welcome")
    public CommonResult<String> welcome() {
        return CommonResult.success("Welcome to Emart E-commerce Platform!");
    }
}
