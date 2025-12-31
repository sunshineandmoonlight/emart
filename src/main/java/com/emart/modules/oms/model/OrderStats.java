package com.emart.modules.oms.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单统计表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_order_stats")
public class OrderStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 统计日期
     */
    private Date date;

    /**
     * 总订单数
     */
    private Integer totalOrders;

    /**
     * 总销售额
     */
    private BigDecimal totalAmount;

    /**
     * 总商品数
     */
    private Integer totalProducts;

    /**
     * 创建时间
     */
    private Date createTime;
}
