package com.emart.modules.pms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品分类参数
 */
@Data
public class CategoryParam {

    private Long id;

    @NotNull(message = "父分类ID不能为空")
    private Long parentId;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @NotNull(message = "分类级别不能为空")
    private Integer level;

    private Integer sort;

    private String icon;
}
