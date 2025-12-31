package com.emart.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.ums.dto.UserLoginParam;
import com.emart.modules.ums.dto.UserRegisterParam;
import com.emart.modules.ums.model.User;

/**
 * 后台用户Service
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    User register(UserRegisterParam param);

    /**
     * 用户登录，返回JWT Token
     */
    String login(UserLoginParam param);

    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);

    /**
     * 根据用户ID获取用户信息（不包含密码）
     */
    User getUserInfo(Long id);

    /**
     * 注销账号
     */
    boolean deleteAccount(Long userId);
}
