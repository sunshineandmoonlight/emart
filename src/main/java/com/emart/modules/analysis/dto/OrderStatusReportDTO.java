package com.emart.modules.analysis.dto;

import lombok.Data;

@Data
public class OrderStatusReportDTO {
    private Integer status;
    private String statusName;
    private Integer count;
}
