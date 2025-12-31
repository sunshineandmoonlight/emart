package com.emart.modules.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.emart.modules.sms.mapper.CouponHistoryMapper;
import com.emart.modules.sms.mapper.CouponMapper;
import com.emart.modules.sms.model.Coupon;
import com.emart.modules.sms.model.CouponHistory;
import com.emart.modules.sms.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 优惠券Service实现类
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponHistoryMapper couponHistoryMapper;

    @Override
    public List<Coupon> getAvailableCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponMapper.findAvailableCoupons(now);
    }

    @Override
    @Transactional
    public CouponHistory receiveCoupon(Long couponId, Long userId) {
        // 获取优惠券信息
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }

        // 检查优惠券状态
        if (coupon.getStatus() != 1) {
            throw new RuntimeException("优惠券未启用");
        }

        LocalDateTime now = LocalDateTime.now();

        // 检查是否在领取时间内
        if (now.isBefore(coupon.getEnableTime()) || now.isAfter(coupon.getDisableTime())) {
            throw new RuntimeException("不在领取时间内");
        }

        // 检查优惠券库存
        if (coupon.getReceiveCount() >= coupon.getCount()) {
            throw new RuntimeException("优惠券已领完");
        }

        // 检查用户领取数量
        Integer userCount = couponHistoryMapper.countByUserIdAndCouponId(userId, couponId);
        if (userCount >= coupon.getPerLimit()) {
            throw new RuntimeException("已达到领取上限");
        }

        // 生成优惠券编码
        String couponCode = generateCouponCode();

        // 计算有效期
        LocalDateTime validityStartTime;
        LocalDateTime validityEndTime;

        if (coupon.getValidityType() == 0) {
            // 固定时间
            validityStartTime = coupon.getStartTime();
            validityEndTime = coupon.getEndTime();
        } else {
            // 领取后N天内有效
            validityStartTime = now;
            validityEndTime = now.plusDays(coupon.getValidityDays());
        }

        // 创建领取记录
        CouponHistory history = new CouponHistory();
        history.setCouponId(couponId);
        history.setUserId(userId);
        history.setCouponCode(couponCode);
        history.setStatus(0); // 未使用
        history.setReceiveTime(now);
        history.setValidityStartTime(validityStartTime);
        history.setValidityEndTime(validityEndTime);

        couponHistoryMapper.insert(history);

        // 更新优惠券领取数量
        coupon.setReceiveCount(coupon.getReceiveCount() + 1);
        couponMapper.updateById(coupon);

        return history;
    }

    @Override
    public List<CouponHistory> getUserCoupons(Long userId, Integer status) {
        return couponHistoryMapper.findByUserId(userId, status);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponMapper.selectById(id);
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        couponMapper.insert(coupon);
        return coupon;
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) {
        couponMapper.updateById(coupon);
        return coupon;
    }

    @Override
    public void deleteCoupon(Long id) {
        couponMapper.deleteById(id);
    }

    /**
     * 生成优惠券编码
     */
    private String generateCouponCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
