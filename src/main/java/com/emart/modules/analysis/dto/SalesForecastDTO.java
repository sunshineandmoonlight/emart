package com.emart.modules.analysis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesForecastDTO {
    private BigDecimal recentAverage;
    private BigDecimal shortTermAverage;
    private BigDecimal forecastAmount;
    private String trend;
    private String evaluation;
}
