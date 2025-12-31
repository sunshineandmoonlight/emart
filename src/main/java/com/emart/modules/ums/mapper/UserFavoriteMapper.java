package com.emart.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.ums.model.UserFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户收藏Mapper
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
}
