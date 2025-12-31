package com.emart.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.pms.model.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Mapper
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
