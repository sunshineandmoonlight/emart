package com.emart.modules.analysis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnalysisOverviewDTO {
    private BigDecimal totalSales;
    private Integer totalOrders;
    private Integer totalProducts;
    private Integer totalUsers;
    private Integer lowStockCount;
    private Integer anomalyCount;
    private Integer browseCount;
}
