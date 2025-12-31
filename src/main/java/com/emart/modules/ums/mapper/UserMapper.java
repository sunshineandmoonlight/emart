package com.emart.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.ums.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
