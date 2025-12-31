package com.emart.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.pms.dto.ProductQueryParam;
import com.emart.modules.pms.dto.ProductSaveParam;
import com.emart.modules.pms.model.Product;

/**
 * 商品Service
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品
     */
    Page<Product> listProducts(ProductQueryParam queryParam);

    /**
     * 获取商品详情
     */
    Product getProductDetail(Long id);

    /**
     * 创建商品
     */
    boolean createProduct(ProductSaveParam productParam);

    /**
     * 更新商品
     */
    boolean updateProduct(Long id, ProductSaveParam productParam);

    /**
     * 删除商品
     */
    boolean deleteProduct(Long id);

    /**
     * 更新商品库存
     */
    boolean updateStock(Long id, Integer quantity);
}
