package com.emart.modules.oms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.admin.dto.SalesStatsDTO;
import com.emart.modules.oms.dto.HotProductDTO;
import com.emart.modules.oms.dto.OrderDetail;
import com.emart.modules.oms.dto.OrderParam;
import com.emart.modules.oms.dto.SalesTrendDTO;
import com.emart.modules.oms.model.Order;

import java.util.List;

/**
 * 订单Service
 */
public interface OrderService extends IService<Order> {

    /**
     * 从购物车创建订单
     */
    Order createOrder(Long userId, String username, OrderParam orderParam);

    /**
     * 根据订单编号获取订单详情
     */
    OrderDetail getOrderByOrderNo(String orderNo);

    /**
     * 分页查询用户订单
     */
    Page<Order> listUserOrders(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 分页查询用户订单详情（包含商品信息）
     */
    Page<OrderDetail> listUserOrdersWithDetails(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(Long orderId, Integer status);

    /**
     * 取消订单
     */
    boolean cancelOrder(Long orderId);

    /**
     * 完成订单
     */
    boolean finishOrder(Long orderId);

    /**
     * 分页查询所有订单（管理员）
     */
    Page<Order> listAllOrders(Integer pageNum, Integer pageSize);

    /**
     * 订单发货（管理员）
     */
    boolean shipOrder(Long orderId, String trackingNo);

    /**
     * 获取销售统计（管理员）
     */
    SalesStatsDTO getSalesStats();

    /**
     * 获取销售趋势（最近7天）
     */
    List<SalesTrendDTO> getSalesTrend();

    /**
     * 获取热门商品（Top10）
     */
    List<HotProductDTO> getHotProducts();
}
