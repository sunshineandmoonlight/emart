package com.emart.modules.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户登录参数")
public class UserLoginParam {

    @Schema(description = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;
}
