package com.emart.modules.pms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.pms.dto.ProductQueryParam;
import com.emart.modules.pms.dto.ProductSaveParam;
import com.emart.modules.pms.mapper.ProductMapper;
import com.emart.modules.pms.model.Product;
import com.emart.modules.pms.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 商品Service实现类
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Page<Product> listProducts(ProductQueryParam queryParam) {
        Page<Product> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        // 商品名称和副标题模糊查询（更准确的搜索）
        if (StrUtil.isNotBlank(queryParam.getName())) {
            // 同时搜索商品名称和副标题，用OR连接
            wrapper.and(w -> w.like(Product::getName, queryParam.getName())
                            .or()
                            .like(Product::getSubtitle, queryParam.getName()));
        }

        // 分类筛选
        if (queryParam.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, queryParam.getCategoryId());
        }

        // 上架状态筛选
        if (queryParam.getStatus() != null) {
            wrapper.eq(Product::getStatus, queryParam.getStatus());
        }

        // 价格范围筛选（如果有价格参数）
        if (queryParam.getMinPrice() != null) {
            wrapper.ge(Product::getPrice, queryParam.getMinPrice());
        }
        if (queryParam.getMaxPrice() != null) {
            wrapper.le(Product::getPrice, queryParam.getMaxPrice());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(Product::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    public Product getProductDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean createProduct(ProductSaveParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);

        // 如果id为0，说明是新增，设置为null让数据库自增
        if (productParam.getId() == 0) {
            product.setId(null);
        }

        return this.save(product);
    }

    @Override
    public boolean updateProduct(Long id, ProductSaveParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setId(id);
        return this.updateById(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean updateStock(Long id, Integer quantity) {
        Product product = this.getById(id);
        if (product == null) {
            log.warn("商品不存在，id: {}", id);
            return false;
        }

        Integer currentStock = product.getStock();
        if (currentStock == null) {
            currentStock = 0;
        }

        // 检查库存是否足够
        if (currentStock < quantity) {
            log.warn("库存不足，当前库存: {}, 需要: {}", currentStock, quantity);
            return false;
        }

        product.setStock(currentStock - quantity);
        return this.updateById(product);
    }
}
