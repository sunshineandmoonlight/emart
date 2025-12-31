package com.emart.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.modules.pms.model.ProductReview;

/**
 * 商品评价Service接口
 */
public interface ReviewService {

    /**
     * 创建商品评价
     */
    boolean createReview(ProductReview review);

    /**
     * 获取商品评价列表（分页）
     */
    Page<ProductReview> getProductReviews(Long productId, Integer pageNum, Integer pageSize);

    /**
     * 获取用户评价列表（分页）
     */
    Page<ProductReview> getUserReviews(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取商品平均评分
     */
    Double getAverageRating(Long productId);

    /**
     * 获取商品评价总数
     */
    Integer getReviewCount(Long productId);

    /**
     * 检查用户是否已评价该订单项
     */
    boolean hasReviewed(Long userId, Long orderItemId);
}
