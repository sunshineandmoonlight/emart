package com.emart.modules.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Schema(description = "销售人员参数")
public class SalesUserParam {

    @NotEmpty(message = "账号不能为空")
    @Size(min = 3, max = 20, message = "账号长度必须在3-20之间")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickName;
}
