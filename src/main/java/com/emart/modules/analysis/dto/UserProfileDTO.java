package com.emart.modules.analysis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserProfileDTO {
    private Long userId;
    private String username;
    private String region;
    private String purchasePower;
    private String favoriteCategory;
    private Integer loginCount;
    private Integer browseCount;
    private Integer orderCount;
    private BigDecimal totalAmount;
}
