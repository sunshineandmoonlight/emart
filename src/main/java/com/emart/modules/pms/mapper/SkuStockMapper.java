package com.emart.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.pms.model.SkuStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * SKU库存Mapper
 */
@Mapper
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    /**
     * 根据商品ID查询SKU列表
     * @param productId 商品ID
     * @return SKU列表
     */
    List<SkuStock> findByProductId(Long productId);
}
