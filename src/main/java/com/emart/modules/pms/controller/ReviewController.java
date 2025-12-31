package com.emart.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.pms.model.ProductReview;
import com.emart.modules.pms.service.ReviewService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品评价Controller
 */
@Slf4j
@RestController
@Tag(name = "商品评价管理", description = "商品评价的增删改查")
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "创建商品评价")
    @PostMapping("/create")
    public CommonResult<Boolean> createReview(@RequestBody ProductReview review,
                                              HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        review.setUserId(userId);
        boolean success = reviewService.createReview(review);
        if (success) {
            return CommonResult.success(true, "评价成功");
        }
        return CommonResult.failed("评价失败或已评价");
    }

    @Operation(summary = "获取商品评价列表（分页）")
    @GetMapping("/product/{productId}")
    public CommonResult<Page<ProductReview>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ProductReview> reviews = reviewService.getProductReviews(productId, pageNum, pageSize);
        return CommonResult.success(reviews);
    }

    @Operation(summary = "获取用户评价列表（分页）")
    @GetMapping("/user")
    public CommonResult<Page<ProductReview>> getUserReviews(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        Page<ProductReview> reviews = reviewService.getUserReviews(userId, pageNum, pageSize);
        return CommonResult.success(reviews);
    }

    @Operation(summary = "获取商品平均评分")
    @GetMapping("/average/{productId}")
    public CommonResult<Double> getAverageRating(@PathVariable Long productId) {
        Double avgRating = reviewService.getAverageRating(productId);
        return CommonResult.success(avgRating);
    }

    @Operation(summary = "获取商品评价总数")
    @GetMapping("/count/{productId}")
    public CommonResult<Integer> getReviewCount(@PathVariable Long productId) {
        Integer count = reviewService.getReviewCount(productId);
        return CommonResult.success(count);
    }

    @Operation(summary = "检查用户是否已评价该订单项")
    @GetMapping("/check/{orderItemId}")
    public CommonResult<Boolean> hasReviewed(@PathVariable Long orderItemId,
                                            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean hasReviewed = reviewService.hasReviewed(userId, orderItemId);
        return CommonResult.success(hasReviewed);
    }

    /**
     * 从JWT Token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        try {
            return jwtTokenUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            log.error("解析Token失败", e);
            return null;
        }
    }
}
