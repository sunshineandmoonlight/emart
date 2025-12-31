package com.emart.modules.sms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户优惠券记录实体类
 */
@Data
@TableName("sms_coupon_history")
public class CouponHistory {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 优惠券编码
     */
    private String couponCode;

    /**
     * 状态；0->未使用；1->已使用；2->已过期
     */
    private Integer status;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 领取时间
     */
    private LocalDateTime receiveTime;

    /**
     * 有效期开始时间
     */
    private LocalDateTime validityStartTime;

    /**
     * 有效期结束时间
     */
    private LocalDateTime validityEndTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
