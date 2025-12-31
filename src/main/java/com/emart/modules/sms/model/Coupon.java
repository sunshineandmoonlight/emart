package com.emart.modules.sms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 */
@Data
@TableName("sms_coupon")
public class Coupon {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券类型；0->满减券；1->折扣券；2->无门槛券
     */
    private Integer type;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 使用平台；0->全平台；1->移动端；2->PC端
     */
    private Integer platform;

    /**
     * 发行数量
     */
    private Integer count;

    /**
     * 每人限领数量
     */
    private Integer perLimit;

    /**
     * 使用门槛金额；0表示无门槛
     */
    private BigDecimal minPoint;

    /**
     * 优惠金额或折扣率
     */
    private BigDecimal amount;

    /**
     * 已领取数量
     */
    private Integer totalCount;

    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 已领取数量
     */
    private Integer receiveCount;

    /**
     * 可以领取的时间
     */
    private LocalDateTime enableTime;

    /**
     * 不可领取的时间
     */
    private LocalDateTime disableTime;

    /**
     * 有效期类型；0->固定时间；1->领取后N天内有效
     */
    private Integer validityType;

    /**
     * 领取后有效天数
     */
    private Integer validityDays;

    /**
     * 有效期开始时间
     */
    private LocalDateTime startTime;

    /**
     * 有效期结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态；0->禁用；1->启用
     */
    private Integer status;

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
