package com.emart.modules.sms.service;

import com.emart.modules.sms.model.Coupon;
import com.emart.modules.sms.model.CouponHistory;

import java.util.List;

/**
 * 优惠券Service接口
 */
public interface CouponService {

    /**
     * 获取可领取的优惠券列表
     * @return 优惠券列表
     */
    List<Coupon> getAvailableCoupons();

    /**
     * 领取优惠券
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return 领取记录
     */
    CouponHistory receiveCoupon(Long couponId, Long userId);

    /**
     * 获取用户的优惠券列表
     * @param userId 用户ID
     * @param status 状态；0->未使用；1->已使用；2->已过期；null->全部
     * @return 用户优惠券列表
     */
    List<CouponHistory> getUserCoupons(Long userId, Integer status);

    /**
     * 根据ID获取优惠券
     * @param id 优惠券ID
     * @return 优惠券对象
     */
    Coupon getCouponById(Long id);

    /**
     * 创建优惠券
     * @param coupon 优惠券对象
     * @return 创建的优惠券对象
     */
    Coupon createCoupon(Coupon coupon);

    /**
     * 更新优惠券
     * @param coupon 优惠券对象
     * @return 更新的优惠券对象
     */
    Coupon updateCoupon(Coupon coupon);

    /**
     * 删除优惠券
     * @param id 优惠券ID
     */
    void deleteCoupon(Long id);
}
