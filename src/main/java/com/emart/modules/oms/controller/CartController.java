package com.emart.modules.oms.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.oms.dto.CartItemParam;
import com.emart.modules.oms.dto.CartUpdateParam;
import com.emart.modules.oms.model.Cart;
import com.emart.modules.oms.service.CartService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 购物车Controller
 */
@Slf4j
@RestController
@Tag(name = "购物车管理", description = "购物车增删改查")
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public CommonResult<Boolean> addCartItem(@Valid @RequestBody CartItemParam cartItemParam,
                                            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = cartService.addCartItem(userId, cartItemParam);
        if (success) {
            return CommonResult.success(true, "添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @Operation(summary = "获取购物车列表")
    @GetMapping("/list")
    public CommonResult<List<Cart>> getCartList(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        List<Cart> cartList = cartService.getCartList(userId);
        return CommonResult.success(cartList);
    }

    @Operation(summary = "更新购物车项数量")
    @PostMapping("/update/{productId}")
    public CommonResult<Boolean> updateCartItem(@PathVariable Long productId,
                                                @Valid @RequestBody CartUpdateParam cartUpdateParam,
                                                HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = cartService.updateCartItem(userId, productId, cartUpdateParam.getQuantity());
        if (success) {
            return CommonResult.success(true, "更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除购物车项")
    @PostMapping("/delete/{productId}")
    public CommonResult<Boolean> deleteCartItem(@PathVariable Long productId,
                                                HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = cartService.deleteCartItem(userId, productId);
        if (success) {
            return CommonResult.success(true, "删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    @Operation(summary = "清空购物车")
    @PostMapping("/clear")
    public CommonResult<Boolean> clearCart(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = cartService.clearCart(userId);
        if (success) {
            return CommonResult.success(true, "清空成功");
        }
        return CommonResult.failed("清空失败");
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
