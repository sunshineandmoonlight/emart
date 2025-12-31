package com.emart.modules.pms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU库存实体类
 */
@Data
@TableName("pms_sku_stock")
public class SkuStock {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * SKU属性JSON数据 (如: {"颜色": "黑色", "容量": "256GB"})
     */
    private String spData;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * SKU价格
     */
    private BigDecimal price;

    /**
     * SKU图片
     */
    private String image;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
