package com.emart.modules.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emart.modules.analysis.dto.*;
import com.emart.modules.analysis.service.AnalysisService;
import com.emart.modules.cms.mapper.BrowseLogMapper;
import com.emart.modules.cms.model.BrowseLog;
import com.emart.modules.log.mapper.LoginLogMapper;
import com.emart.modules.log.model.LoginLog;
import com.emart.modules.oms.mapper.OrderItemMapper;
import com.emart.modules.oms.mapper.OrderMapper;
import com.emart.modules.oms.model.Order;
import com.emart.modules.oms.model.OrderItem;
import com.emart.modules.pms.mapper.CategoryMapper;
import com.emart.modules.pms.mapper.ProductMapper;
import com.emart.modules.pms.model.Category;
import com.emart.modules.pms.model.Product;
import com.emart.modules.ums.mapper.UserMapper;
import com.emart.modules.ums.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BrowseLogMapper browseLogMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public AnalysisOverviewDTO getOverview() {
        List<Order> orders = paidOrders();
        List<Product> products = productMapper.selectList(null);
        List<SalesAnomalyDTO> anomalies = getAnomalies();

        AnalysisOverviewDTO dto = new AnalysisOverviewDTO();
        dto.setTotalSales(sumOrderAmount(orders));
        dto.setTotalOrders(orders.size());
        dto.setTotalProducts(products.size());
        dto.setTotalUsers(userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getRole, "CUSTOMER")).intValue());
        dto.setLowStockCount((int) products.stream().filter(product -> safeStock(product) < 10).count());
        dto.setAnomalyCount(anomalies.size());
        dto.setBrowseCount(browseLogMapper.selectCount(null).intValue());
        return dto;
    }

    @Override
    public List<SalesTrendPointDTO> getSalesTrend(String range) {
        List<Order> orders = paidOrders();
        String normalizedRange = range == null ? "day" : range;
        Map<String, List<Order>> grouped = orders.stream()
                .collect(Collectors.groupingBy(order -> trendLabel(order.getCreateTime(), normalizedRange)));

        List<String> labels = trendLabels(normalizedRange);
        return labels.stream().map(label -> {
            List<Order> items = grouped.getOrDefault(label, Collections.emptyList());
            SalesTrendPointDTO dto = new SalesTrendPointDTO();
            dto.setLabel(label);
            dto.setAmount(sumOrderAmount(items));
            dto.setCount(items.size());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public SalesForecastDTO getSalesForecast() {
        List<Order> orders = paidOrders();
        BigDecimal last7Days = averageDailySales(orders, 7);
        BigDecimal last3Days = averageDailySales(orders, 3);
        BigDecimal upper = last7Days.multiply(BigDecimal.valueOf(1.2));
        BigDecimal lower = last7Days.multiply(BigDecimal.valueOf(0.8));

        String trend = "稳定";
        if (last3Days.compareTo(upper) > 0) {
            trend = "上升";
        } else if (last3Days.compareTo(lower) < 0) {
            trend = "下降";
        }

        SalesForecastDTO dto = new SalesForecastDTO();
        dto.setRecentAverage(last7Days);
        dto.setShortTermAverage(last3Days);
        dto.setForecastAmount(last7Days.multiply(BigDecimal.valueOf(7)).setScale(2, RoundingMode.HALF_UP));
        dto.setTrend(trend);
        dto.setEvaluation("近3日均值为 " + last3Days + "，近7日均值为 " + last7Days + "，销售趋势判断为" + trend);
        return dto;
    }

    @Override
    public List<ProductRankingDTO> getProductRanking(String range) {
        Set<Long> orderIds = filterOrdersByRange(paidOrders(), range).stream()
                .map(Order::getId)
                .collect(Collectors.toSet());
        return orderItemsInOrders(orderIds).stream()
                .collect(Collectors.groupingBy(OrderItem::getProductId))
                .entrySet().stream()
                .map(entry -> {
                    List<OrderItem> items = entry.getValue();
                    OrderItem first = items.get(0);
                    ProductRankingDTO dto = new ProductRankingDTO();
                    dto.setProductId(entry.getKey());
                    dto.setProductName(first.getProductName());
                    dto.setSalesCount(items.stream().mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity()).sum());
                    dto.setSalesAmount(sumItemAmount(items));
                    return dto;
                })
                .sorted((a, b) -> b.getSalesCount().compareTo(a.getSalesCount()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorySalesDTO> getCategorySales() {
        Map<Long, Product> productMap = productMapper.selectList(null).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (a, b) -> a));
        Map<Long, Category> categoryMap = categoryMapper.selectList(null).stream()
                .collect(Collectors.toMap(Category::getId, Function.identity(), (a, b) -> a));

        Map<Long, List<OrderItem>> grouped = orderItemMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(item -> {
                    Product product = productMap.get(item.getProductId());
                    return product == null || product.getCategoryId() == null ? 0L : product.getCategoryId();
                }));

        return grouped.entrySet().stream().map(entry -> {
            Long categoryId = entry.getKey();
            Category category = categoryMap.get(categoryId);
            CategorySalesDTO dto = new CategorySalesDTO();
            dto.setCategoryId(categoryId);
            dto.setCategoryName(category == null ? "未分类" : category.getName());
            dto.setSalesCount(entry.getValue().stream().mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity()).sum());
            dto.setSalesAmount(sumItemAmount(entry.getValue()));
            return dto;
        }).sorted((a, b) -> b.getSalesAmount().compareTo(a.getSalesAmount())).collect(Collectors.toList());
    }

    @Override
    public List<StockReportDTO> getStockReport() {
        List<Product> products = productMapper.selectList(null);
        List<StockReportDTO> result = new ArrayList<>();
        result.add(stockBucket("缺货", products, 0, 0));
        result.add(stockBucket("低库存(1-9)", products, 1, 9));
        result.add(stockBucket("正常库存(10-99)", products, 10, 99));
        result.add(stockBucket("高库存(100+)", products, 100, Integer.MAX_VALUE));
        return result;
    }

    @Override
    public List<OrderStatusReportDTO> getOrderStatusReport() {
        Map<Integer, Long> statusCount = orderMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        List<OrderStatusReportDTO> result = new ArrayList<>();
        Map<Integer, String> names = new LinkedHashMap<>();
        names.put(0, "待付款");
        names.put(1, "待发货");
        names.put(2, "已发货");
        names.put(3, "已完成");
        names.put(4, "已取消");
        names.forEach((status, name) -> {
            OrderStatusReportDTO dto = new OrderStatusReportDTO();
            dto.setStatus(status);
            dto.setStatusName(name);
            dto.setCount(statusCount.getOrDefault(status, 0L).intValue());
            result.add(dto);
        });
        return result;
    }

    @Override
    public List<SalesAnomalyDTO> getAnomalies() {
        List<SalesAnomalyDTO> result = new ArrayList<>();
        List<Product> products = productMapper.selectList(null);
        Map<Long, Integer> salesCountMap = orderItemMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(OrderItem::getProductId,
                        Collectors.summingInt(item -> item.getQuantity() == null ? 0 : item.getQuantity())));

        products.stream().filter(product -> safeStock(product) < 10).forEach(product -> {
            SalesAnomalyDTO dto = new SalesAnomalyDTO();
            dto.setType("LOW_STOCK");
            dto.setTargetName(product.getName());
            dto.setCurrentValue(String.valueOf(safeStock(product)));
            dto.setReferenceValue("库存阈值：10");
            dto.setSuggestion("建议销售人员及时补货或下架缺货商品");
            result.add(dto);
        });

        products.stream()
                .filter(product -> safeStock(product) > 100 && salesCountMap.getOrDefault(product.getId(), 0) == 0)
                .forEach(product -> {
                    SalesAnomalyDTO dto = new SalesAnomalyDTO();
                    dto.setType("HIGH_STOCK_SLOW_SALE");
                    dto.setTargetName(product.getName());
                    dto.setCurrentValue("库存 " + safeStock(product) + "，销量 0");
                    dto.setReferenceValue("高库存且无销售");
                    dto.setSuggestion("建议调整价格、增加曝光或设置促销活动");
                    result.add(dto);
                });

        List<Order> orders = paidOrders();
        BigDecimal avg = orders.isEmpty() ? BigDecimal.ZERO :
                sumOrderAmount(orders).divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);
        orders.stream()
                .filter(order -> avg.compareTo(BigDecimal.ZERO) > 0 && order.getTotalAmount().compareTo(avg.multiply(BigDecimal.valueOf(3))) > 0)
                .forEach(order -> {
                    SalesAnomalyDTO dto = new SalesAnomalyDTO();
                    dto.setType("HIGH_VALUE_ORDER");
                    dto.setTargetName(order.getOrderNo());
                    dto.setCurrentValue(order.getTotalAmount().toPlainString());
                    dto.setReferenceValue("平均订单金额：" + avg.toPlainString());
                    dto.setSuggestion("建议确认支付与发货信息，避免高金额异常订单风险");
                    result.add(dto);
                });

        return result.stream().limit(20).collect(Collectors.toList());
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        return buildUserProfile(user);
    }

    @Override
    public List<UserProfileDTO> listUserProfiles() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRole, "CUSTOMER");
        wrapper.orderByDesc(User::getCreateTime);
        return userMapper.selectList(wrapper).stream()
                .map(this::buildUserProfile)
                .collect(Collectors.toList());
    }

    private UserProfileDTO buildUserProfile(User user) {
        List<Order> userOrders = paidOrders().stream()
                .filter(order -> Objects.equals(order.getUserId(), user.getId()))
                .collect(Collectors.toList());
        List<BrowseLog> browseLogs = browseLogMapper.selectList(new LambdaQueryWrapper<BrowseLog>().eq(BrowseLog::getUserId, user.getId()));
        List<LoginLog> loginLogs = loginLogMapper.selectList(new LambdaQueryWrapper<LoginLog>().eq(LoginLog::getUserId, user.getId()));

        String favoriteCategory = browseLogs.stream()
                .filter(log -> log.getCategoryName() != null)
                .collect(Collectors.groupingBy(BrowseLog::getCategoryName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("暂无");

        BigDecimal totalAmount = sumOrderAmount(userOrders);
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRegion(resolveRegion(loginLogs));
        dto.setPurchasePower(resolvePurchasePower(totalAmount));
        dto.setFavoriteCategory(favoriteCategory);
        dto.setLoginCount(loginLogs.size());
        dto.setBrowseCount(browseLogs.size());
        dto.setOrderCount(userOrders.size());
        dto.setTotalAmount(totalAmount);
        return dto;
    }

    private List<Order> paidOrders() {
        return orderMapper.selectList(new LambdaQueryWrapper<Order>().in(Order::getStatus, 1, 2, 3));
    }

    private List<Order> filterOrdersByRange(List<Order> orders, String range) {
        LocalDate start = LocalDate.now().minusDays(30);
        if ("day".equals(range)) {
            start = LocalDate.now().minusDays(7);
        } else if ("week".equals(range)) {
            start = LocalDate.now().minusWeeks(4);
        } else if ("month".equals(range)) {
            start = LocalDate.now().minusMonths(6);
        }
        LocalDate finalStart = start;
        return orders.stream()
                .filter(order -> order.getCreateTime() != null)
                .filter(order -> toLocalDate(order.getCreateTime()).compareTo(finalStart) >= 0)
                .collect(Collectors.toList());
    }

    private List<OrderItem> orderItemsInOrders(Set<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return orderItemMapper.selectList(null).stream()
                .filter(item -> orderIds.contains(item.getOrderId()))
                .collect(Collectors.toList());
    }

    private BigDecimal sumOrderAmount(List<Order> orders) {
        return orders.stream()
                .map(Order::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal averageDailySales(List<Order> orders, int days) {
        LocalDate start = LocalDate.now().minusDays(days - 1);
        BigDecimal total = orders.stream()
                .filter(order -> order.getCreateTime() != null)
                .filter(order -> toLocalDate(order.getCreateTime()).compareTo(start) >= 0)
                .map(Order::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumItemAmount(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getTotalAmount() != null
                        ? item.getTotalAmount()
                        : item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity() == null ? 0 : item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String trendLabel(Date date, String range) {
        LocalDate localDate = toLocalDate(date);
        if ("week".equals(range)) {
            int week = localDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            return localDate.getYear() + "-W" + week;
        }
        if ("month".equals(range)) {
            return localDate.getYear() + "-" + String.format("%02d", localDate.getMonthValue());
        }
        return new SimpleDateFormat("MM-dd").format(date);
    }

    private List<String> trendLabels(String range) {
        LocalDate today = LocalDate.now();
        List<String> labels = new ArrayList<>();
        if ("week".equals(range)) {
            for (int i = 3; i >= 0; i--) {
                LocalDate date = today.minusWeeks(i);
                labels.add(date.getYear() + "-W" + date.get(WeekFields.ISO.weekOfWeekBasedYear()));
            }
        } else if ("month".equals(range)) {
            for (int i = 5; i >= 0; i--) {
                LocalDate date = today.minusMonths(i);
                labels.add(date.getYear() + "-" + String.format("%02d", date.getMonthValue()));
            }
        } else {
            for (int i = 6; i >= 0; i--) {
                Date date = Date.from(today.minusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
                labels.add(new SimpleDateFormat("MM-dd").format(date));
            }
        }
        return labels;
    }

    private StockReportDTO stockBucket(String label, List<Product> products, int min, int max) {
        StockReportDTO dto = new StockReportDTO();
        dto.setLabel(label);
        dto.setCount((int) products.stream().filter(product -> safeStock(product) >= min && safeStock(product) <= max).count());
        return dto;
    }

    private int safeStock(Product product) {
        return product.getStock() == null ? 0 : product.getStock();
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String resolvePurchasePower(BigDecimal totalAmount) {
        if (totalAmount.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            return "高购买力";
        }
        if (totalAmount.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return "中购买力";
        }
        return "低购买力";
    }

    private String resolveRegion(List<LoginLog> loginLogs) {
        return loginLogs.stream()
                .map(LoginLog::getIp)
                .filter(Objects::nonNull)
                .findFirst()
                .map(ip -> ip.startsWith("127.") || ip.startsWith("0:0:0") || "localhost".equals(ip) ? "本地访问" : "IP:" + ip)
                .orElse("未知");
    }
}
