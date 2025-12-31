package com.emart.modules.oms.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.admin.dto.SalesStatsDTO;
import com.emart.modules.oms.dto.OrderDetail;
import com.emart.modules.oms.dto.OrderParam;
import com.emart.modules.oms.mapper.CartMapper;
import com.emart.modules.oms.mapper.OrderItemMapper;
import com.emart.modules.oms.mapper.OrderMapper;
import com.emart.modules.oms.model.*;
import com.emart.modules.oms.service.OrderService;
import com.emart.modules.pms.mapper.ProductMapper;
import com.emart.modules.pms.model.Product;
import com.emart.modules.ums.mapper.UserMapper;
import com.emart.modules.ums.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单Service实现类
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, String username, OrderParam orderParam) {
        // 1. 查询用户购物车
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId);
        List<Cart> cartList = cartMapper.selectList(cartWrapper);

        if (cartList == null || cartList.isEmpty()) {
            log.warn("购物车为空，无法创建订单，userId: {}", userId);
            return null;
        }

        // 2. 计算订单总金额并验证库存
        BigDecimal totalAmount = new BigDecimal("0");
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cart : cartList) {
            // 查询商品信息
            Product product = productMapper.selectById(cart.getProductId());
            if (product == null) {
                log.warn("商品不存在，productId: {}", cart.getProductId());
                continue;
            }

            // 检查库存
            if (product.getStock() == null || product.getStock() < cart.getQuantity()) {
                log.warn("商品库存不足，productId: {}, 库存: {}, 需要: {}",
                    cart.getProductId(), product.getStock(), cart.getQuantity());
                throw new RuntimeException("商品 [" + product.getName() + "] 库存不足");
            }

            // 计算小计
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalAmount(itemTotal);

            orderItems.add(orderItem);
        }

        if (orderItems.isEmpty()) {
            log.warn("没有有效的商品，无法创建订单");
            return null;
        }

        // 3. 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setUsername(username);
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 待付款
        order.setReceiverName(orderParam.getReceiverName());
        order.setReceiverPhone(orderParam.getReceiverPhone());
        order.setReceiverAddress(orderParam.getReceiverAddress());
        order.setCreateTime(new Date());

        orderMapper.insert(order);

        // 4. 创建订单项
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
            orderItemMapper.insert(orderItem);
        }

        // 5. 扣减库存
        for (OrderItem orderItem : orderItems) {
            Product product = productMapper.selectById(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateById(product);
        }

        // 6. 清空购物车
        cartMapper.delete(cartWrapper);

        log.info("订单创建成功，orderNo: {}, userId: {}, totalAmount: {}",
            order.getOrderNo(), userId, totalAmount);

        return order;
    }

    @Override
    public OrderDetail getOrderByOrderNo(String orderNo) {
        // 查询订单
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(orderWrapper);

        if (order == null) {
            return null;
        }

        // 查询订单项
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> orderItems = orderItemMapper.selectList(itemWrapper);

        // 组装DTO
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(order.getId());
        orderDetail.setOrderNo(order.getOrderNo());
        orderDetail.setUserId(order.getUserId());
        orderDetail.setUsername(order.getUsername());
        orderDetail.setTotalAmount(order.getTotalAmount());
        orderDetail.setStatus(order.getStatus());
        orderDetail.setReceiverName(order.getReceiverName());
        orderDetail.setReceiverPhone(order.getReceiverPhone());
        orderDetail.setReceiverAddress(order.getReceiverAddress());
        orderDetail.setPayTime(formatDate(order.getPayTime()));
        orderDetail.setDeliveryTime(formatDate(order.getDeliveryTime()));
        orderDetail.setFinishTime(formatDate(order.getFinishTime()));
        orderDetail.setCreateTime(formatDate(order.getCreateTime()));
        orderDetail.setOrderItems(orderItems);

        return orderDetail;
    }

    @Override
    public Page<Order> listUserOrders(Long userId, Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
               .orderByDesc(Order::getCreateTime);
        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<OrderDetail> listUserOrdersWithDetails(Long userId, Integer pageNum, Integer pageSize) {
        // 1. 查询订单列表
        Page<Order> orderPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
               .orderByDesc(Order::getCreateTime);
        orderMapper.selectPage(orderPage, wrapper);

        // 2. 转换为OrderDetail并填充订单项
        Page<OrderDetail> detailPage = new Page<>(pageNum, pageSize, orderPage.getTotal());
        List<OrderDetail> detailList = new ArrayList<>();

        for (Order order : orderPage.getRecords()) {
            // 查询订单项
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, order.getId());
            List<OrderItem> orderItems = orderItemMapper.selectList(itemWrapper);

            // 构建OrderDetail
            OrderDetail detail = new OrderDetail();
            detail.setId(order.getId());
            detail.setOrderNo(order.getOrderNo());
            detail.setUserId(order.getUserId());
            detail.setUsername(order.getUsername());
            detail.setTotalAmount(order.getTotalAmount());
            detail.setStatus(order.getStatus());
            detail.setReceiverName(order.getReceiverName());
            detail.setReceiverPhone(order.getReceiverPhone());
            detail.setReceiverAddress(order.getReceiverAddress());
            detail.setPayTime(formatDate(order.getPayTime()));
            detail.setDeliveryTime(formatDate(order.getDeliveryTime()));
            detail.setFinishTime(formatDate(order.getFinishTime()));
            detail.setCreateTime(formatDate(order.getCreateTime()));
            detail.setOrderItems(orderItems);

            detailList.add(detail);
        }

        detailPage.setRecords(detailList);
        return detailPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, Integer status) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            log.warn("订单不存在，orderId: {}", orderId);
            return false;
        }

        order.setStatus(status);

        // 根据状态更新相应的时间字段
        Date now = new Date();
        if (status == 1) {
            // 待发货
            order.setPayTime(now);
        } else if (status == 2) {
            // 已发货
            order.setDeliveryTime(now);
        } else if (status == 3) {
            // 已完成
            order.setFinishTime(now);
        }

        int result = orderMapper.updateById(order);
        log.info("订单状态更新成功，orderId: {}, status: {}", orderId, status);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, 4); // 4-已取消
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishOrder(Long orderId) {
        return updateOrderStatus(orderId, 3); // 3-已完成
    }

    @Override
    public Page<Order> listAllOrders(Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean shipOrder(Long orderId, String trackingNo) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            log.warn("订单不存在，orderId: {}", orderId);
            return false;
        }

        if (order.getStatus() != 1) {
            log.warn("订单状态不正确，无法发货，orderId: {}, status: {}", orderId, order.getStatus());
            return false;
        }

        order.setStatus(2); // 已发货
        order.setDeliveryTime(new Date());
        int result = orderMapper.updateById(order);
        log.info("订单发货成功，orderId: {}, trackingNo: {}", orderId, trackingNo);
        return result > 0;
    }

    @Override
    public SalesStatsDTO getSalesStats() {
        SalesStatsDTO stats = new SalesStatsDTO();

        // 今日订单数和销售额
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.between(Order::getCreateTime,
            java.sql.Timestamp.valueOf(todayStart),
            java.sql.Timestamp.valueOf(todayEnd));

        List<Order> todayOrders = orderMapper.selectList(todayWrapper);
        stats.setTodayOrders(todayOrders.size());

        BigDecimal todaySales = todayOrders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTodaySales(todaySales);

        // 总商品数
        Long totalProducts = productMapper.selectCount(null);
        stats.setTotalProducts(totalProducts.intValue());

        // 总用户数
        Long totalUsers = userMapper.selectCount(null);
        stats.setTotalUsers(totalUsers.intValue());

        return stats;
    }

    @Override
    public List<com.emart.modules.oms.dto.SalesTrendDTO> getSalesTrend() {
        List<com.emart.modules.oms.dto.SalesTrendDTO> trendList = new ArrayList<>();

        // 获取最近7天的日期
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(formatter);

            // 查询当天的订单
            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.between(Order::getCreateTime,
                java.sql.Timestamp.valueOf(dayStart),
                java.sql.Timestamp.valueOf(dayEnd));

            List<Order> orders = orderMapper.selectList(wrapper);

            // 计算当天的销售额和订单数
            BigDecimal amount = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            com.emart.modules.oms.dto.SalesTrendDTO trendDTO = new com.emart.modules.oms.dto.SalesTrendDTO();
            trendDTO.setDate(dateStr);
            trendDTO.setAmount(amount);
            trendDTO.setCount(orders.size());

            trendList.add(trendDTO);
        }

        return trendList;
    }

    @Override
    public List<com.emart.modules.oms.dto.HotProductDTO> getHotProducts() {
        // 查询订单项，按商品ID分组统计销售数量
        LambdaQueryWrapper<com.emart.modules.oms.model.OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            com.emart.modules.oms.model.OrderItem::getProductId,
            com.emart.modules.oms.model.OrderItem::getProductName,
            com.emart.modules.oms.model.OrderItem::getPrice
        );

        List<com.emart.modules.oms.model.OrderItem> allItems = orderItemMapper.selectList(null);

        // 按商品ID分组统计
        Map<Long, List<com.emart.modules.oms.model.OrderItem>> productGroups = allItems.stream()
            .collect(Collectors.groupingBy(com.emart.modules.oms.model.OrderItem::getProductId));

        List<com.emart.modules.oms.dto.HotProductDTO> hotProducts = productGroups.entrySet().stream()
            .map(entry -> {
                Long productId = entry.getKey();
                List<com.emart.modules.oms.model.OrderItem> items = entry.getValue();

                com.emart.modules.oms.dto.HotProductDTO dto = new com.emart.modules.oms.dto.HotProductDTO();
                dto.setProductId(productId);

                if (!items.isEmpty()) {
                    com.emart.modules.oms.model.OrderItem firstItem = items.get(0);
                    dto.setProductName(firstItem.getProductName());

                    // 计算销售数量
                    Integer salesCount = items.stream()
                        .mapToInt(com.emart.modules.oms.model.OrderItem::getQuantity)
                        .sum();
                    dto.setSalesCount(salesCount);

                    // 计算销售额
                    Double salesAmount = items.stream()
                        .mapToDouble(item -> item.getPrice().doubleValue() * item.getQuantity())
                        .sum();
                    dto.setSalesAmount(salesAmount);
                }

                return dto;
            })
            .sorted((p1, p2) -> p2.getSalesCount().compareTo(p1.getSalesCount())) // 按销售数量降序
            .limit(10) // 取前10名
            .collect(Collectors.toList());

        return hotProducts;
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return "ORDER" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
