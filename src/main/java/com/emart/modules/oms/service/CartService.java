package com.emart.modules.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.oms.dto.CartItemParam;
import com.emart.modules.oms.model.Cart;

import java.util.List;

/**
 * 购物车Service
 */
public interface CartService extends IService<Cart> {

    /**
     * 添加商品到购物车
     */
    boolean addCartItem(Long userId, CartItemParam cartItemParam);

    /**
     * 获取用户购物车列表
     */
    List<Cart> getCartList(Long userId);

    /**
     * 更新购物车项数量
     */
    boolean updateCartItem(Long userId, Long productId, Integer quantity);

    /**
     * 删除购物车项
     */
    boolean deleteCartItem(Long userId, Long productId);

    /**
     * 清空购物车
     */
    boolean clearCart(Long userId);
}
