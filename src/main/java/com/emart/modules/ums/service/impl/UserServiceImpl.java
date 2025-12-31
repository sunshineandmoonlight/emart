package com.emart.modules.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.ums.dto.UserLoginParam;
import com.emart.modules.ums.dto.UserRegisterParam;
import com.emart.modules.ums.mapper.UserMapper;
import com.emart.modules.ums.model.User;
import com.emart.modules.ums.service.UserService;
import com.emart.security.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 后台用户Service实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User register(UserRegisterParam param) {
        User user = new User();
        user.setUsername(param.getUsername());
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        user.setEmail(param.getEmail());
        user.setNickName(param.getNickName());
        user.setStatus(1);
        user.setCreateTime(new Date());

        // 保存到数据库
        this.save(user);

        log.info("用户注册成功：{}", param.getUsername());
        return user;
    }

    @Override
    public String login(UserLoginParam param) {
        // 根据用户名查询用户
        User user = getUserByUsername(param.getUsername());

        if (user == null) {
            log.warn("登录失败：用户不存在 - {}", param.getUsername());
            return null;
        }

        // 验证密码
        if (!passwordEncoder.matches(param.getPassword(), user.getPassword())) {
            log.warn("登录失败：密码错误 - {}", param.getUsername());
            return null;
        }

        // 检查状态
        if (user.getStatus() == 0) {
            log.warn("登录失败：账号已被禁用 - {}", param.getUsername());
            return null;
        }

        // 更新最后登录时间
        user.setLoginTime(new Date());
        this.updateById(user);

        log.info("用户登录成功：{}", param.getUsername());

        // 生成并返回 JWT Token
        return jwtTokenUtil.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public User getUserInfo(Long id) {
        User user = this.getById(id);
        if (user != null) {
            // 清空密码，不返回给前端
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public boolean deleteAccount(Long userId) {
        try {
            // 检查用户是否存在
            User user = this.getById(userId);
            if (user == null) {
                log.warn("注销账号失败：用户不存在 - userId: {}", userId);
                return false;
            }

            // 删除用户（软删除或硬删除，这里使用硬删除）
            boolean success = this.removeById(userId);

            if (success) {
                log.info("账号注销成功：userId: {}, username: {}", userId, user.getUsername());
            }

            return success;
        } catch (Exception e) {
            log.error("注销账号失败：userId: {}", userId, e);
            throw new RuntimeException("注销账号失败: " + e.getMessage());
        }
    }
}
