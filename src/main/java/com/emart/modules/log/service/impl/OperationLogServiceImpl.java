package com.emart.modules.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.log.mapper.OperationLogMapper;
import com.emart.modules.log.model.OperationLog;
import com.emart.modules.log.service.OperationLogService;
import com.emart.modules.log.util.RequestIpUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public void record(Long adminId, String username, String role, String operationType, String operationContent, HttpServletRequest request) {
        OperationLog operationLog = new OperationLog();
        operationLog.setAdminId(adminId);
        operationLog.setUsername(username);
        operationLog.setRole(role);
        operationLog.setOperationType(operationType);
        operationLog.setOperationContent(operationContent);
        operationLog.setRequestUri(request == null ? null : request.getRequestURI());
        operationLog.setIp(RequestIpUtil.getClientIp(request));
        operationLog.setCreateTime(new Date());
        this.save(operationLog);
    }
}
