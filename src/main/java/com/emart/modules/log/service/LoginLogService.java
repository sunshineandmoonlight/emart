package com.emart.modules.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.log.model.LoginLog;

import javax.servlet.http.HttpServletRequest;

public interface LoginLogService extends IService<LoginLog> {

    void record(Long userId, String username, String role, boolean success, String message, HttpServletRequest request);
}
