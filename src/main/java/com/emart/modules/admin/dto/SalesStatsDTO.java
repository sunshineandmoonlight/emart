package com.emart.modules.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售统计DTO
 */
@Data
public class SalesStatsDTO {
    // 今日订单数
    private Integer todayOrders;
    // 今日销售额
    private BigDecimal todaySales;
    // 总商品数
    private Integer totalProducts;
    // 总用户数
    private Integer totalUsers;
}
