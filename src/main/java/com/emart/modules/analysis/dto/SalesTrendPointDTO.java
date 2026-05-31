package com.emart.modules.analysis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesTrendPointDTO {
    private String label;
    private BigDecimal amount;
    private Integer count;
}
