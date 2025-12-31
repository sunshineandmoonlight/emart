package com.emart.modules.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.sms.model.Coupon;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券Mapper
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 查询可领取的优惠券列表
     * @param now 当前时间
     * @return 可领取的优惠券列表
     */
    List<Coupon> findAvailableCoupons(LocalDateTime now);
}
