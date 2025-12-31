package com.emart.modules.sms.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.sms.model.Coupon;
import com.emart.modules.sms.model.CouponHistory;
import com.emart.modules.sms.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券Controller
 */
@RestController
@Tag(name = "优惠券管理", description = "优惠券管理接口")
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 获取可领取的优惠券列表
     */
    @Operation(summary = "获取可领取的优惠券列表")
    @GetMapping("/list")
    public CommonResult<List<Coupon>> getAvailableCoupons() {
        List<Coupon> coupons = couponService.getAvailableCoupons();
        return CommonResult.success(coupons);
    }

    /**
     * 领取优惠券
     */
    @Operation(summary = "领取优惠券")
    @PostMapping("/receive/{couponId}")
    public CommonResult<CouponHistory> receiveCoupon(@PathVariable Long couponId, @RequestHeader("userId") Long userId) {
        try {
            CouponHistory history = couponService.receiveCoupon(couponId, userId);
            return CommonResult.success(history);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 获取我的优惠券列表
     */
    @Operation(summary = "获取我的优惠券列表")
    @GetMapping("/my")
    public CommonResult<List<CouponHistory>> getMyCoupons(
            @RequestHeader("userId") Long userId,
            @RequestParam(required = false) Integer status
    ) {
        List<CouponHistory> coupons = couponService.getUserCoupons(userId, status);
        return CommonResult.success(coupons);
    }

    /**
     * 根据ID获取优惠券
     */
    @Operation(summary = "获取优惠券详情")
    @GetMapping("/{id}")
    public CommonResult<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        if (coupon == null) {
            return CommonResult.failed("优惠券不存在");
        }
        return CommonResult.success(coupon);
    }

    /**
     * 创建优惠券（管理端）
     */
    @Operation(summary = "创建优惠券")
    @PostMapping("/create")
    public CommonResult<Coupon> createCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon created = couponService.createCoupon(coupon);
            return CommonResult.success(created);
        } catch (Exception e) {
            return CommonResult.failed("创建优惠券失败: " + e.getMessage());
        }
    }

    /**
     * 更新优惠券（管理端）
     */
    @Operation(summary = "更新优惠券")
    @PutMapping("/update")
    public CommonResult<Coupon> updateCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon updated = couponService.updateCoupon(coupon);
            return CommonResult.success(updated);
        } catch (Exception e) {
            return CommonResult.failed("更新优惠券失败: " + e.getMessage());
        }
    }

    /**
     * 删除优惠券（管理端）
     */
    @Operation(summary = "删除优惠券")
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteCoupon(@PathVariable Long id) {
        try {
            couponService.deleteCoupon(id);
            return CommonResult.success();
        } catch (Exception e) {
            return CommonResult.failed("删除优惠券失败: " + e.getMessage());
        }
    }
}
