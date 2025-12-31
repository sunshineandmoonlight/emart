package com.emart.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.modules.pms.model.Product;
import com.emart.modules.ums.model.UserFavorite;

import java.util.List;

/**
 * 收藏服务接口
 */
public interface FavoriteService {

    /**
     * 添加收藏
     */
    boolean addFavorite(Long userId, Long productId);

    /**
     * 取消收藏
     */
    boolean removeFavorite(Long userId, Long productId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Long productId);

    /**
     * 获取用户收藏列表（分页）
     */
    Page<Product> getUserFavorites(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取用户收藏的商品ID列表
     */
    List<Long> getUserFavoriteProductIds(Long userId);
}
