package com.emart.modules.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.log.mapper.LoginLogMapper;
import com.emart.modules.log.model.LoginLog;
import com.emart.modules.log.service.LoginLogService;
import com.emart.modules.log.util.RequestIpUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public void record(Long userId, String username, String role, boolean success, String message, HttpServletRequest request) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setRole(role);
        loginLog.setLoginTime(new Date());
        loginLog.setIp(RequestIpUtil.getClientIp(request));
        loginLog.setUserAgent(request == null ? null : request.getHeader("User-Agent"));
        loginLog.setSuccess(success ? 1 : 0);
        loginLog.setMessage(message);
        loginLog.setCreateTime(new Date());
        this.save(loginLog);
    }
}
