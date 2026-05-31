package com.emart.modules.oms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单创建参数
 */
@Data
public class OrderParam {

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    /**
     * 直接购买商品项。为空时沿用购物车下单流程。
     */
    @Valid
    private List<OrderItemParam> items;

    @Data
    public static class OrderItemParam {

        @NotNull(message = "商品ID不能为空")
        private Long productId;

        @NotNull(message = "购买数量不能为空")
        @Min(value = 1, message = "购买数量至少为1")
        private Integer quantity;
    }
}
