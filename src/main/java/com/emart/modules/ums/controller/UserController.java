package com.emart.modules.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.common.service.MailService;
import com.emart.modules.ums.dto.UserLoginParam;
import com.emart.modules.ums.dto.UserRegisterParam;
import com.emart.modules.ums.model.User;
import com.emart.modules.ums.service.UserService;
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
 * 后台用户管理Controller
 */
@Slf4j
@RestController
@Tag(name = "用户管理", description = "用户注册、登录、信息管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public CommonResult<User> register(@Valid @RequestBody UserRegisterParam param) {
        User user = userService.register(param);
        if (user == null) {
            return CommonResult.failed("注册失败");
        }

        // 发送注册成功邮件（异步处理，不阻塞主流程）
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("username", user.getUsername());
            variables.put("email", user.getEmail());

            mailService.sendTemplateMail(
                user.getEmail(),
                "欢迎加入E-mart电商平台",
                "register-success",
                variables
            );
            log.info("注册成功邮件已发送至：{}", user.getEmail());
        } catch (Exception e) {
            log.error("发送注册邮件失败：{}", e.getMessage());
            // 邮件发送失败不影响注册流程
        }

        // 清空密码后返回
        user.setPassword(null);
        return CommonResult.success(user, "注册成功");
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@Valid @RequestBody UserLoginParam param) {
        String token = userService.login(param);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return CommonResult.success(tokenMap, "登录成功");
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public CommonResult<User> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        // TODO: 从 JWT Token 中解析用户ID
        // 暂时先返回空，后续实现 JWT 认证过滤器后再完善
        return CommonResult.failed("请先登录");
    }

    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public CommonResult<String> logout() {
        return CommonResult.success(null, "退出成功");
    }

    @Operation(summary = "注销账号")
    @DeleteMapping("/delete-account")
    public CommonResult<String> deleteAccount(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // TODO: 从JWT Token中获取用户ID
        // 暂时先简单实现，后续从Token中解析userId
        try {
            // 这里需要从Token中解析userId，暂时使用固定逻辑
            // 实际应该从JWT中获取
            boolean success = userService.deleteAccount(1L); // TODO: 替换为从Token获取的真实userId
            if (success) {
                return CommonResult.success(null, "账号注销成功");
            }
            return CommonResult.failed("账号注销失败");
        } catch (Exception e) {
            log.error("注销账号失败", e);
            return CommonResult.failed("注销失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取所有用户列表（管理员）")
    @GetMapping("/list")
    public CommonResult<Page<User>> listAllUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> userPage = userService.page(page, wrapper);

        // 清空密码
        userPage.getRecords().forEach(user -> user.setPassword(null));

        return CommonResult.success(userPage);
    }

    @Operation(summary = "更新用户状态（管理员）")
    @PostMapping("/status/{userId}")
    public CommonResult<Boolean> updateUserStatus(@PathVariable Long userId,
                                                  @RequestParam Integer status) {
        User user = userService.getById(userId);
        if (user == null) {
            return CommonResult.failed("用户不存在");
        }

        user.setStatus(status);
        boolean success = userService.updateById(user);
        if (success) {
            return CommonResult.success(true, "状态更新成功");
        }
        return CommonResult.failed("状态更新失败");
    }
}
