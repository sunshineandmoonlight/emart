package com.emart.modules.payment.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.emart.common.api.CommonResult;
import com.emart.config.AlipayProperties;
import com.emart.modules.oms.model.Order;
import com.emart.modules.oms.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 支付Controller
 */
@Slf4j
@RestController
@Tag(name = "支付管理", description = "支付宝支付")
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private OrderService orderService;

    /**
     * 发起支付
     */
    @Operation(summary = "发起支付")
    @GetMapping(value = "/pay/{orderId}", produces = "text/html;charset=UTF-8")
    public String pay(@PathVariable Long orderId) throws Exception {
        // 查询订单
        Order order = orderService.getById(orderId);
        if (order == null) {
            return "<html><body><h3>订单不存在</h3></body></html>";
        }

        // 创建支付请求
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(alipayProperties.getReturnUrl());
        request.setNotifyUrl(alipayProperties.getNotifyUrl());

        // 设置支付参数
        request.setBizContent("{\"out_trade_no\":\"" + order.getOrderNo() + "\","
                + "\"total_amount\":\"" + order.getTotalAmount() + "\","
                + "\"subject\":\"订单支付\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 调用支付宝支付接口
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

        if (response.isSuccess()) {
            log.info("订单{}发起支付成功", order.getOrderNo());
            // 直接返回支付表单HTML
            return response.getBody();
        } else {
            log.error("订单{}发起支付失败：{}", order.getOrderNo(), response.getSubMsg());
            return "<html><body><h3>支付发起失败：" + response.getSubMsg() + "</h3></body></html>";
        }
    }

    /**
     * 支付同步回调
     */
    @Operation(summary = "支付同步回调")
    @GetMapping("/return")
    public void returnCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取支付参数
        String outTradeNo = request.getParameter("out_trade_no"); // 商户订单号
        String tradeNo = request.getParameter("trade_no"); // 支付宝交易号
        String totalAmount = request.getParameter("total_amount"); // 支付金额
        String tradeStatus = request.getParameter("trade_status"); // 交易状态

        log.info("支付同步回调：订单号={}, 交易号={}, 金额={}, 状态={}",
                outTradeNo, tradeNo, totalAmount, tradeStatus);

        // 更新订单状态
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            var order = orderService.getOrderByOrderNo(outTradeNo);
            if (order != null) {
                orderService.updateOrderStatus(order.getId(), 1); // 更新为已付款
            }
        }

        // 重定向到前端订单页面
        response.sendRedirect("http://localhost:5174/order");
    }

    /**
     * 支付异步通知
     */
    @Operation(summary = "支付异步通知")
    @PostMapping("/notify")
    public String notifyCallback(HttpServletRequest request) {
        // 获取支付参数
        String outTradeNo = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");
        String totalAmount = request.getParameter("total_amount");
        String tradeStatus = request.getParameter("trade_status");

        log.info("支付异步通知：订单号={}, 交易号={}, 金额={}, 状态={}",
                outTradeNo, tradeNo, totalAmount, tradeStatus);

        // 验证签名（实际生产环境需要验证）
        // 更新订单状态
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            var order = orderService.getOrderByOrderNo(outTradeNo);
            if (order != null) {
                orderService.updateOrderStatus(order.getId(), 1); // 更新为已付款
            }
        }

        // 返回success告诉支付宝已处理
        return "success";
    }
}
