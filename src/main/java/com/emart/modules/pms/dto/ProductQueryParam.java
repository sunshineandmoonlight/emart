package com.emart.modules.pms.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品查询参数
 */
@Data
public class ProductQueryParam {

    /**
     * 商品名称（模糊查询，搜索名称和副标题）
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 上架状态
     */
    private Integer status;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;
}
