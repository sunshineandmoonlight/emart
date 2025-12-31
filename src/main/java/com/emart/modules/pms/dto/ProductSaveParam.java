package com.emart.modules.pms.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 商品添加/编辑参数
 */
@Data
public class ProductSaveParam {

    @NotNull(message = "商品ID不能为空")
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200")
    private String name;

    @Size(max = 200, message = "副标题长度不能超过200")
    private String subtitle;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;

    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    private Long categoryId;

    @Size(max = 100, message = "品牌长度不能超过100")
    private String brand;

    @Size(max = 500, message = "商品主图路径长度不能超过500")
    private String image;

    private String images;

    private String detail;

    @NotNull(message = "上架状态不能为空")
    private Integer status;
}
