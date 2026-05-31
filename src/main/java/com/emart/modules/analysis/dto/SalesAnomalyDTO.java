package com.emart.modules.analysis.dto;

import lombok.Data;

@Data
public class SalesAnomalyDTO {
    private String type;
    private String targetName;
    private String currentValue;
    private String referenceValue;
    private String suggestion;
}
