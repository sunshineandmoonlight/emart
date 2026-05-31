package com.emart.modules.log.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 */
@Data
@TableName("sys_login_log")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private Long userId;

    private String username;

    private String role;

    private Date loginTime;

    private String ip;

    private String userAgent;

    private Integer success;

    private String message;

    private Date createTime;
}
