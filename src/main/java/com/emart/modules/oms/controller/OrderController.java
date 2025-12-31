package com.emart.modules.oms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.common.service.MailService;
import com.emart.modules.admin.dto.SalesStatsDTO;
import com.emart.modules.oms.dto.OrderDetail;
import com.emart.modules.oms.dto.OrderParam;
import com.emart.modules.oms.model.Order;
import com.emart.modules.oms.service.OrderService;
import com.emart.modules.ums.model.User;
import com.emart.modules.ums.service.UserService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单Controller
 */
@Slf4j
@RestController
@Tag(name = "订单管理", description = "订单创建、查询、状态更新")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Operation(summary = "从购物车创建订单")
    @PostMapping("/create")
    public CommonResult<Order> createOrder(@Valid @RequestBody OrderParam orderParam,
                                          HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        String userName = getUserNameFromToken(request);
        if (userName == null) {
            userName = "User" + userId;
        }

        try {
            Order order = orderService.createOrder(userId, userName, orderParam);
            if (order != null) {
                // 发送订单确认邮件
                try {
                    User user = userService.getById(userId);
                    if (user != null && user.getEmail() != null) {
                        OrderDetail orderDetail = orderService.getOrderByOrderNo(order.getOrderNo());

                        Map<String, Object> variables = new HashMap<>();
                        variables.put("username", userName);
                        variables.put("orderNo", order.getOrderNo());
                        variables.put("createTime", order.getCreateTime());
                        variables.put("status", "待付款");
                        variables.put("receiverName", order.getReceiverName());
                        variables.put("receiverPhone", order.getReceiverPhone());
                        variables.put("receiverAddress", order.getReceiverAddress());
                        variables.put("orderItems", orderDetail.getOrderItems());
                        variables.put("totalAmount", order.getTotalAmount());

                        mailService.sendTemplateMail(
                            user.getEmail(),
                            "订单创建成功 - " + order.getOrderNo(),
                            "order-confirm",
                            variables
                        );
                        log.info("订单确认邮件已发送至：{}", user.getEmail());
                    }
                } catch (Exception e) {
                    log.error("发送订单邮件失败：{}", e.getMessage());
                    // 邮件发送失败不影响订单创建流程
                }

                return CommonResult.success(order, "订单创建成功");
            }
            return CommonResult.failed("购物车为空或商品库存不足");
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return CommonResult.failed(e.getMessage());
        }
    }

    @Operation(summary = "根据订单编号获取订单详情")
    @GetMapping("/detail/{orderNo}")
    public CommonResult<OrderDetail> getOrderByOrderNo(@PathVariable String orderNo) {
        OrderDetail orderDetail = orderService.getOrderByOrderNo(orderNo);
        if (orderDetail == null) {
            return CommonResult.failed("订单不存在");
        }
        return CommonResult.success(orderDetail);
    }

    @Operation(summary = "查询当前用户的订单列表")
    @GetMapping("/list")
    public CommonResult<Page<Order>> listUserOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        Page<Order> orders = orderService.listUserOrders(userId, pageNum, pageSize);
        return CommonResult.success(orders);
    }

    @Operation(summary = "查询当前用户的订单详情列表（包含商品信息）")
    @GetMapping("/list-with-details")
    public CommonResult<Page<com.emart.modules.oms.dto.OrderDetail>> listUserOrdersWithDetails(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        Page<com.emart.modules.oms.dto.OrderDetail> orders = orderService.listUserOrdersWithDetails(userId, pageNum, pageSize);
        return CommonResult.success(orders);
    }

    @Operation(summary = "取消订单")
    @PostMapping("/cancel/{orderId}")
    public CommonResult<Boolean> cancelOrder(@PathVariable Long orderId,
                                            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = orderService.cancelOrder(orderId);
        if (success) {
            return CommonResult.success(true, "订单已取消");
        }
        return CommonResult.failed("取消订单失败");
    }

    @Operation(summary = "完成订单")
    @PostMapping("/finish/{orderId}")
    public CommonResult<Boolean> finishOrder(@PathVariable Long orderId,
                                            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return CommonResult.unauthorized(null);
        }

        boolean success = orderService.finishOrder(orderId);
        if (success) {
            return CommonResult.success(true, "订单已完成");
        }
        return CommonResult.failed("完成订单失败");
    }

    @Operation(summary = "更新订单状态")
    @PostMapping("/status/{orderId}")
    public CommonResult<Boolean> updateOrderStatus(@PathVariable Long orderId,
                                                  @RequestParam Integer status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        if (success) {
            return CommonResult.success(true, "订单状态已更新");
        }
        return CommonResult.failed("更新订单状态失败");
    }

    @Operation(summary = "获取所有订单（管理员）")
    @GetMapping("/all")
    public CommonResult<Page<Order>> listAllOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Order> orders = orderService.listAllOrders(pageNum, pageSize);
        return CommonResult.success(orders);
    }

    @Operation(summary = "订单发货（管理员）")
    @PostMapping("/ship/{orderId}")
    public CommonResult<Boolean> shipOrder(@PathVariable Long orderId,
                                         @RequestParam String trackingNo) {
        boolean success = orderService.shipOrder(orderId, trackingNo);
        if (success) {
            return CommonResult.success(true, "发货成功");
        }
        return CommonResult.failed("发货失败");
    }

    @Operation(summary = "获取销售统计（管理员）")
    @GetMapping("/stats")
    public CommonResult<SalesStatsDTO> getSalesStats() {
        SalesStatsDTO stats = orderService.getSalesStats();
        return CommonResult.success(stats);
    }

    @Operation(summary = "获取销售趋势（最近7天）")
    @GetMapping("/sales-trend")
    public CommonResult<java.util.List<com.emart.modules.oms.dto.SalesTrendDTO>> getSalesTrend() {
        java.util.List<com.emart.modules.oms.dto.SalesTrendDTO> trend = orderService.getSalesTrend();
        return CommonResult.success(trend);
    }

    @Operation(summary = "获取热门商品（Top10）")
    @GetMapping("/hot-products")
    public CommonResult<java.util.List<com.emart.modules.oms.dto.HotProductDTO>> getHotProducts() {
        java.util.List<com.emart.modules.oms.dto.HotProductDTO> hotProducts = orderService.getHotProducts();
        return CommonResult.success(hotProducts);
    }

    /**
     * 从JWT Token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        try {
            return jwtTokenUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            log.error("解析Token失败", e);
            return null;
        }
    }

    /**
     * 从JWT Token中获取用户名
     */
    private String getUserNameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        try {
            return jwtTokenUtil.getUserNameFromToken(token);
        } catch (Exception e) {
            log.error("解析Token失败", e);
            return null;
        }
    }
}
