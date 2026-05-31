package com.emart.modules.analysis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategorySalesDTO {
    private Long categoryId;
    private String categoryName;
    private Integer salesCount;
    private BigDecimal salesAmount;
}
