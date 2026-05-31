package com.emart.modules.analysis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRankingDTO {
    private Long productId;
    private String productName;
    private Integer salesCount;
    private BigDecimal salesAmount;
}
