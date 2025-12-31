package com.emart.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.modules.pms.mapper.ProductMapper;
import com.emart.modules.pms.model.Product;
import com.emart.modules.ums.mapper.UserFavoriteMapper;
import com.emart.modules.ums.model.UserFavorite;
import com.emart.modules.ums.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 */
@Slf4j
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public boolean addFavorite(Long userId, Long productId) {
        // 检查是否已收藏
        if (isFavorited(userId, productId)) {
            log.warn("商品已收藏，userId: {}, productId: {}", userId, productId);
            return false;
        }

        // 检查商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            log.warn("商品不存在，productId: {}", productId);
            return false;
        }

        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);

        int result = userFavoriteMapper.insert(favorite);
        log.info("添加收藏成功，userId: {}, productId: {}", userId, productId);
        return result > 0;
    }

    @Override
    public boolean removeFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
               .eq(UserFavorite::getProductId, productId);

        int result = userFavoriteMapper.delete(wrapper);
        log.info("取消收藏，userId: {}, productId: {}, result: {}", userId, productId, result);
        return result > 0;
    }

    @Override
    public boolean isFavorited(Long userId, Long productId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
               .eq(UserFavorite::getProductId, productId);

        return userFavoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Page<Product> getUserFavorites(Long userId, Integer pageNum, Integer pageSize) {
        // 1. 查询用户收藏的商品ID列表
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
               .orderByDesc(UserFavorite::getCreateTime);

        Page<UserFavorite> favoritePage = new Page<>(pageNum, pageSize);
        userFavoriteMapper.selectPage(favoritePage, wrapper);

        // 2. 提取商品ID列表
        List<Long> productIds = favoritePage.getRecords().stream()
                .map(UserFavorite::getProductId)
                .collect(Collectors.toList());

        // 3. 查询商品详细信息
        if (productIds.isEmpty()) {
            return new Page<>(pageNum, pageSize);
        }

        Page<Product> productPage = new Page<>(pageNum, pageSize);
        productPage.setRecords(productMapper.selectBatchIds(productIds));
        productPage.setTotal(favoritePage.getTotal());

        return productPage;
    }

    @Override
    public List<Long> getUserFavoriteProductIds(Long userId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);

        return userFavoriteMapper.selectList(wrapper).stream()
                .map(UserFavorite::getProductId)
                .collect(Collectors.toList());
    }
}
