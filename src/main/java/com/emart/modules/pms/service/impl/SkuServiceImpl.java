package com.emart.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.emart.modules.pms.mapper.SkuStockMapper;
import com.emart.modules.pms.model.SkuStock;
import com.emart.modules.pms.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SKU库存Service实现类
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuStockMapper skuStockMapper;

    @Override
    public List<SkuStock> getSkusByProductId(Long productId) {
        return skuStockMapper.findByProductId(productId);
    }

    @Override
    public SkuStock getSkuById(Long id) {
        return skuStockMapper.selectById(id);
    }

    @Override
    public SkuStock createSku(SkuStock skuStock) {
        skuStockMapper.insert(skuStock);
        return skuStock;
    }

    @Override
    public SkuStock updateSku(SkuStock skuStock) {
        skuStockMapper.updateById(skuStock);
        return skuStock;
    }

    @Override
    public void deleteSku(Long id) {
        skuStockMapper.deleteById(id);
    }

    @Override
    public void updateStock(Long skuId, Integer quantity) {
        SkuStock sku = getSkuById(skuId);
        if (sku != null) {
            Integer newStock = sku.getStock() + quantity;
            if (newStock < 0) {
                throw new RuntimeException("库存不足");
            }
            sku.setStock(newStock);
            skuStockMapper.updateById(sku);
        }
    }
}
