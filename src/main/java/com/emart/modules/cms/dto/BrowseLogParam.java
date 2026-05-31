package com.emart.modules.cms.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BrowseLogParam {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    private Integer durationSeconds;
}
