package com.emart.modules.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.log.model.LoginLog;
import com.emart.modules.log.model.OperationLog;
import com.emart.modules.log.service.LoginLogService;
import com.emart.modules.log.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "日志管理", description = "登录日志、后台操作日志")
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private OperationLogService operationLogService;

    @Operation(summary = "分页查询登录日志")
    @GetMapping("/login/list")
    public CommonResult<Page<LoginLog>> listLoginLogs(@RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      @RequestParam(required = false) String role) {
        Page<LoginLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(role != null && role.length() > 0, LoginLog::getRole, role);
        wrapper.orderByDesc(LoginLog::getLoginTime);
        return CommonResult.success(loginLogService.page(page, wrapper));
    }

    @Operation(summary = "分页查询后台操作日志")
    @GetMapping("/operation/list")
    public CommonResult<Page<OperationLog>> listOperationLogs(@RequestParam(defaultValue = "1") Integer pageNum,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              @RequestParam(required = false) String role) {
        Page<OperationLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(role != null && role.length() > 0, OperationLog::getRole, role);
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return CommonResult.success(operationLogService.page(page, wrapper));
    }
}
