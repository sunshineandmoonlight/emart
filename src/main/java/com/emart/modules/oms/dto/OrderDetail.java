package com.emart.modules.oms.dto;

import com.emart.modules.oms.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情DTO
 */
@Data
public class OrderDetail {

    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private BigDecimal totalAmount;
    private Integer status;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String payTime;
    private String deliveryTime;
    private String finishTime;
    private String createTime;

    /**
     * 订单项列表
     */
    private List<OrderItem> orderItems;
}
