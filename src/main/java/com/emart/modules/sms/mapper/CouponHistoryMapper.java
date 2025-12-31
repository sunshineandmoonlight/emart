package com.emart.modules.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.sms.model.CouponHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户优惠券记录Mapper
 */
@Mapper
public interface CouponHistoryMapper extends BaseMapper<CouponHistory> {

    /**
     * 查询用户的优惠券列表
     * @param userId 用户ID
     * @param status 状态；0->未使用；1->已使用；2->已过期；null->全部
     * @return 用户优惠券列表
     */
    List<CouponHistory> findByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 统计用户已领取的某优惠券数量
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 领取数量
     */
    Integer countByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);
}
