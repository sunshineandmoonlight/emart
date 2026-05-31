package com.emart.modules.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.admin.dto.ResetPasswordParam;
import com.emart.modules.admin.dto.SalesUserParam;
import com.emart.modules.log.service.LoginLogService;
import com.emart.modules.log.service.OperationLogService;
import com.emart.modules.ums.model.User;
import com.emart.modules.ums.dto.UserLoginParam;
import com.emart.modules.ums.service.UserService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
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

    @Autowired
    private UserService userService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private OperationLogService operationLogService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@Valid @RequestBody UserLoginParam param,
                                                   HttpServletRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, param.getUsername());
        wrapper.in(User::getRole, "ADMIN", "SALES");
        User admin = userService.getOne(wrapper);

        if (admin == null || !passwordEncoder.matches(param.getPassword(), admin.getPassword())) {
            loginLogService.record(null, param.getUsername(), "ADMIN", false, "后台账号或密码错误", request);
            return CommonResult.validateFailed("后台账号或密码错误");
        }
        if (admin.getStatus() == null || admin.getStatus() == 0) {
            loginLogService.record(admin.getId(), admin.getUsername(), admin.getRole(), false, "账号已禁用", request);
            return CommonResult.validateFailed("账号已禁用");
        }

        admin.setLoginTime(new java.util.Date());
        userService.updateById(admin);

        String token = jwtTokenUtil.generateToken(admin.getId(), admin.getUsername());
        loginLogService.record(admin.getId(), admin.getUsername(), admin.getRole(), true, "登录成功", request);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("username", admin.getUsername());
        tokenMap.put("role", admin.getRole());

        return CommonResult.success(tokenMap, "登录成功");
    }

    @Operation(summary = "分页查询销售人员")
    @GetMapping("/sales/list")
    public CommonResult<Page<User>> listSales(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              HttpServletRequest request) {
        User currentAdmin = getCurrentAdmin(request);
        if (currentAdmin == null || !"ADMIN".equals(currentAdmin.getRole())) {
            return CommonResult.forbidden(null);
        }

        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRole, "SALES");
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> result = userService.page(page, wrapper);
        result.setTotal(userService.count(wrapper));
        result.getRecords().forEach(user -> user.setPassword(null));
        return CommonResult.success(result);
    }

    @Operation(summary = "添加销售人员")
    @PostMapping("/sales/create")
    public CommonResult<Boolean> createSales(@Valid @RequestBody SalesUserParam param,
                                             HttpServletRequest request) {
        User currentAdmin = getCurrentAdmin(request);
        if (currentAdmin == null || !"ADMIN".equals(currentAdmin.getRole())) {
            return CommonResult.forbidden(false);
        }

        if (userService.getUserByUsername(param.getUsername()) != null) {
            return CommonResult.validateFailed("账号已存在");
        }

        User sales = new User();
        sales.setUsername(param.getUsername());
        sales.setPassword(passwordEncoder.encode(param.getPassword()));
        sales.setEmail(param.getEmail());
        sales.setNickName(param.getNickName());
        sales.setStatus(1);
        sales.setRole("SALES");
        sales.setCreateTime(new Date());
        boolean success = userService.save(sales);
        if (success) {
            operationLogService.record(currentAdmin.getId(), currentAdmin.getUsername(), currentAdmin.getRole(), "CREATE_SALES",
                    "添加销售人员：" + param.getUsername(), request);
            return CommonResult.success(true, "销售人员添加成功");
        }
        return CommonResult.failed("销售人员添加失败");
    }

    @Operation(summary = "删除销售人员")
    @PostMapping("/sales/delete/{id}")
    public CommonResult<Boolean> deleteSales(@PathVariable Long id,
                                             HttpServletRequest request) {
        User currentAdmin = getCurrentAdmin(request);
        if (currentAdmin == null || !"ADMIN".equals(currentAdmin.getRole())) {
            return CommonResult.forbidden(false);
        }

        User sales = userService.getById(id);
        if (sales == null || !"SALES".equals(sales.getRole())) {
            return CommonResult.failed("销售人员不存在");
        }

        boolean success = userService.removeById(id);
        if (success) {
            operationLogService.record(currentAdmin.getId(), currentAdmin.getUsername(), currentAdmin.getRole(), "DELETE_SALES",
                    "删除销售人员：" + sales.getUsername(), request);
            return CommonResult.success(true, "销售人员删除成功");
        }
        return CommonResult.failed("销售人员删除失败");
    }

    @Operation(summary = "重置销售人员密码")
    @PostMapping("/sales/reset-password/{id}")
    public CommonResult<Boolean> resetSalesPassword(@PathVariable Long id,
                                                    @Valid @RequestBody ResetPasswordParam param,
                                                    HttpServletRequest request) {
        User currentAdmin = getCurrentAdmin(request);
        if (currentAdmin == null || !"ADMIN".equals(currentAdmin.getRole())) {
            return CommonResult.forbidden(false);
        }

        User sales = userService.getById(id);
        if (sales == null || !"SALES".equals(sales.getRole())) {
            return CommonResult.failed("销售人员不存在");
        }

        sales.setPassword(passwordEncoder.encode(param.getPassword()));
        boolean success = userService.updateById(sales);
        if (success) {
            operationLogService.record(currentAdmin.getId(), currentAdmin.getUsername(), currentAdmin.getRole(), "RESET_SALES_PASSWORD",
                    "重置销售人员密码：" + sales.getUsername(), request);
            return CommonResult.success(true, "密码重置成功");
        }
        return CommonResult.failed("密码重置失败");
    }

    private User getCurrentAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        Long userId = jwtTokenUtil.getUserIdFromToken(authHeader.substring(7));
        if (userId == null) {
            return null;
        }
        return userService.getById(userId);
    }
}
