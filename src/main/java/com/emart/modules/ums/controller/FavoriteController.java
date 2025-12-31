package com.emart.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.pms.model.Product;
import com.emart.modules.ums.service.FavoriteService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 收藏Controller
 */
@Slf4j
@RestController
@Tag(name = "收藏管理", description = "商品收藏、取消收藏、收藏列表")
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "添加收藏")
    @PostMapping("/add/{productId}")
    public CommonResult<Boolean> addFavorite(@PathVariable Long productId,
                                            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = favoriteService.addFavorite(userId, productId);
        if (success) {
            return CommonResult.success(true, "收藏成功");
        }
        return CommonResult.failed("收藏失败或已收藏");
    }

    @Operation(summary = "取消收藏")
    @PostMapping("/remove/{productId}")
    public CommonResult<Boolean> removeFavorite(@PathVariable Long productId,
                                               HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = favoriteService.removeFavorite(userId, productId);
        if (success) {
            return CommonResult.success(true, "取消收藏成功");
        }
        return CommonResult.failed("取消收藏失败");
    }

    @Operation(summary = "检查是否已收藏")
    @GetMapping("/check/{productId}")
    public CommonResult<Boolean> isFavorited(@PathVariable Long productId,
                                            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean favorited = favoriteService.isFavorited(userId, productId);
        return CommonResult.success(favorited);
    }

    @Operation(summary = "获取收藏列表（分页）")
    @GetMapping("/list")
    public CommonResult<Page<Product>> getUserFavorites(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        Page<Product> products = favoriteService.getUserFavorites(userId, pageNum, pageSize);
        return CommonResult.success(products);
    }

    @Operation(summary = "获取用户收藏的商品ID列表")
    @GetMapping("/product-ids")
    public CommonResult<List<Long>> getUserFavoriteProductIds(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        List<Long> productIds = favoriteService.getUserFavoriteProductIds(userId);
        return CommonResult.success(productIds);
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
