package com.emart.modules.log.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台操作日志
 */
@Data
@TableName("sys_operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private Long adminId;

    private String username;

    private String role;

    private String operationType;

    private String operationContent;

    private String requestUri;

    private String ip;

    private Date createTime;
}
