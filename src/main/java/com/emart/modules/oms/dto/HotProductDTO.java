package com.emart.modules.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门商品DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotProductDTO {
    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 销售数量
     */
    private Integer salesCount;

    /**
     * 销售额
     */
    private Double salesAmount;
}
