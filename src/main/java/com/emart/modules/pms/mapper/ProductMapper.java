package com.emart.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.pms.model.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
