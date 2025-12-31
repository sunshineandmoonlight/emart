package com.emart.modules.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 销售趋势DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesTrendDTO {
    /**
     * 日期（格式：yyyy-MM-dd）
     */
    private String date;

    /**
     * 销售额
     */
    private BigDecimal amount;

    /**
     * 订单数量
     */
    private Integer count;
}
