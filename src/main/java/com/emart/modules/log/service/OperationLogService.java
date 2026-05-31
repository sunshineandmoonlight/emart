package com.emart.modules.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.log.model.OperationLog;

import javax.servlet.http.HttpServletRequest;

public interface OperationLogService extends IService<OperationLog> {

    void record(Long adminId, String username, String role, String operationType, String operationContent, HttpServletRequest request);
}
