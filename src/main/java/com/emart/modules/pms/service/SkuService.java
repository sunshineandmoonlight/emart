package com.emart.modules.pms.service;

import com.emart.modules.pms.model.SkuStock;

import java.util.List;

/**
 * SKU库存Service接口
 */
public interface SkuService {

    /**
     * 根据商品ID获取SKU列表
     * @param productId 商品ID
     * @return SKU列表
     */
    List<SkuStock> getSkusByProductId(Long productId);

    /**
     * 根据ID获取SKU
     * @param id SKU ID
     * @return SKU对象
     */
    SkuStock getSkuById(Long id);

    /**
     * 创建SKU
     * @param skuStock SKU对象
     * @return 创建的SKU对象
     */
    SkuStock createSku(SkuStock skuStock);

    /**
     * 更新SKU
     * @param skuStock SKU对象
     * @return 更新的SKU对象
     */
    SkuStock updateSku(SkuStock skuStock);

    /**
     * 删除SKU
     * @param id SKU ID
     */
    void deleteSku(Long id);

    /**
     * 更新SKU库存
     * @param skuId SKU ID
     * @param quantity 数量（正数为增加，负数为减少）
     */
    void updateStock(Long skuId, Integer quantity);
}
