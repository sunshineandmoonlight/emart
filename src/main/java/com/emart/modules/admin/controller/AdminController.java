package com.emart.modules.admin.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.ums.dto.UserLoginParam;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员Controller
 */
@Slf4j
@RestController
@Tag(name = "管理员管理", description = "管理员登录、认证")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@Valid @RequestBody UserLoginParam param) {
        // 验证管理员账号（简化版：硬编码管理员账号）
        if (!"admin".equals(param.getUsername()) || !"admin123".equals(param.getPassword())) {
            return CommonResult.validateFailed("管理员账号或密码错误");
        }

        // 直接生成token
        String token = jwtTokenUtil.generateToken(0L, param.getUsername()); // 0L表示管理员用户ID

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("username", param.getUsername());

        return CommonResult.success(tokenMap, "登录成功");
    }
}
