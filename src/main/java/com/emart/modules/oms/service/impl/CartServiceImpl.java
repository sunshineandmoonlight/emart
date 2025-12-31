package com.emart.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.oms.dto.CartItemParam;
import com.emart.modules.oms.mapper.CartMapper;
import com.emart.modules.pms.mapper.ProductMapper;
import com.emart.modules.oms.model.Cart;
import com.emart.modules.pms.model.Product;
import com.emart.modules.oms.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 购物车Service实现类
 */
@Slf4j
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CART_KEY_PREFIX = "oms:cart:";

    @Override
    public boolean addCartItem(Long userId, CartItemParam cartItemParam) {
        // 查询是否已存在
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, cartItemParam.getProductId());
        Cart existCart = cartMapper.selectOne(wrapper);

        if (existCart != null) {
            // 已存在，更新数量
            existCart.setQuantity(existCart.getQuantity() + cartItemParam.getQuantity());
            cartMapper.updateById(existCart);
        } else {
            // 不存在，新增
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(cartItemParam.getProductId());
            cart.setQuantity(cartItemParam.getQuantity());
            cartMapper.insert(cart);
        }

        // 清除Redis缓存
        clearCartCache(userId);
        return true;
    }

    @Override
    public List<Cart> getCartList(Long userId) {
        // 先从Redis缓存获取
        String cacheKey = CART_KEY_PREFIX + userId;
        List<Cart> cartList = (List<Cart>) redisTemplate.opsForValue().get(cacheKey);

        if (cartList != null) {
            // 如果缓存中有数据，仍然需要填充商品信息（因为缓存可能不包含商品详情）
            fillProductInfo(cartList);
            return cartList;
        }

        // 缓存不存在，从数据库查询
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        cartList = cartMapper.selectList(wrapper);

        // 填充商品信息
        fillProductInfo(cartList);

        // 存入Redis缓存，30分钟过期
        redisTemplate.opsForValue().set(cacheKey, cartList, 30, TimeUnit.MINUTES);

        return cartList;
    }

    /**
     * 填充购物车商品信息
     */
    private void fillProductInfo(List<Cart> cartList) {
        if (cartList == null || cartList.isEmpty()) {
            return;
        }

        // 提取所有商品ID
        Set<Long> productIds = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());

        if (productIds.isEmpty()) {
            return;
        }

        // 批量查询商品信息
        List<Product> products = productMapper.selectBatchIds(productIds);

        // 转为Map方便查找
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        // 填充商品信息到购物车
        for (Cart cart : cartList) {
            Product product = productMap.get(cart.getProductId());
            cart.setProduct(product);
        }
    }

    @Override
    public boolean updateCartItem(Long userId, Long productId, Integer quantity) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, productId);

        Cart cart = cartMapper.selectOne(wrapper);
        if (cart == null) {
            log.warn("购物车项不存在，userId: {}, productId: {}", userId, productId);
            return false;
        }

        if (quantity == 0) {
            // 数量为0，删除购物车项
            cartMapper.delete(wrapper);
        } else {
            // 更新数量
            cart.setQuantity(quantity);
            cartMapper.updateById(cart);
        }

        // 清除Redis缓存
        clearCartCache(userId);
        return true;
    }

    @Override
    public boolean deleteCartItem(Long userId, Long productId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, productId);

        int result = cartMapper.delete(wrapper);

        // 清除Redis缓存
        clearCartCache(userId);
        return result > 0;
    }

    @Override
    public boolean clearCart(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);

        int result = cartMapper.delete(wrapper);

        // 清除Redis缓存
        clearCartCache(userId);
        return result > 0;
    }

    /**
     * 清除购物车缓存
     */
    private void clearCartCache(Long userId) {
        String cacheKey = CART_KEY_PREFIX + userId;
        redisTemplate.delete(cacheKey);
    }
}
