package com.emart.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.modules.pms.mapper.ProductReviewMapper;
import com.emart.modules.pms.model.ProductReview;
import com.emart.modules.pms.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 商品评价Service实现
 */
@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Override
    public boolean createReview(ProductReview review) {
        // 检查是否已评价
        if (hasReviewed(review.getUserId(), review.getOrderItemId())) {
            log.warn("用户已评价该订单项，userId: {}, orderItemId: {}", review.getUserId(), review.getOrderItemId());
            return false;
        }

        // 验证评分
        if (review.getRating() < 1 || review.getRating() > 5) {
            log.warn("评分必须在1-5之间，rating: {}", review.getRating());
            return false;
        }

        int result = productReviewMapper.insert(review);
        if (result > 0) {
            log.info("评价创建成功，userId: {}, productId: {}, rating: {}",
                    review.getUserId(), review.getProductId(), review.getRating());
            return true;
        }
        return false;
    }

    @Override
    public Page<ProductReview> getProductReviews(Long productId, Integer pageNum, Integer pageSize) {
        Page<ProductReview> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<ProductReview> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductReview::getProductId, productId)
                .orderByDesc(ProductReview::getCreateTime);

        return productReviewMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<ProductReview> getUserReviews(Long userId, Integer pageNum, Integer pageSize) {
        Page<ProductReview> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<ProductReview> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductReview::getUserId, userId)
                .orderByDesc(ProductReview::getCreateTime);

        return productReviewMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Double getAverageRating(Long productId) {
        BigDecimal avgRating = productReviewMapper.getAverageRating(productId);
        if (avgRating == null) {
            return 0.0;
        }
        return avgRating.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Integer getReviewCount(Long productId) {
        return productReviewMapper.getReviewCount(productId);
    }

    @Override
    public boolean hasReviewed(Long userId, Long orderItemId) {
        LambdaQueryWrapper<ProductReview> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductReview::getUserId, userId)
                .eq(ProductReview::getOrderItemId, orderItemId);

        Long count = productReviewMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
}
