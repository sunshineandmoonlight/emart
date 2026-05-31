package com.emart.modules.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emart.modules.cms.mapper.BrowseLogMapper;
import com.emart.modules.cms.model.BrowseLog;
import com.emart.modules.oms.mapper.OrderItemMapper;
import com.emart.modules.oms.mapper.OrderMapper;
import com.emart.modules.oms.model.Order;
import com.emart.modules.oms.model.OrderItem;
import com.emart.modules.pms.mapper.ProductMapper;
import com.emart.modules.pms.model.Product;
import com.emart.modules.recommend.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BrowseLogMapper browseLogMapper;

    @Override
    public List<Product> alsoBuy(Long productId, Integer limit) {
        int size = normalizeLimit(limit);
        Product current = productMapper.selectById(productId);
        if (current == null) {
            return Collections.emptyList();
        }

        List<Order> orders = orderMapper.selectList(null);
        Map<Long, Order> orderMap = orders.stream().collect(Collectors.toMap(Order::getId, order -> order, (a, b) -> a));
        List<OrderItem> items = orderItemMapper.selectList(null);

        Set<Long> userIds = items.stream()
                .filter(item -> Objects.equals(item.getProductId(), productId))
                .map(item -> orderMap.get(item.getOrderId()))
                .filter(Objects::nonNull)
                .map(Order::getUserId)
                .collect(Collectors.toSet());

        Map<Long, Double> scoreMap = new HashMap<>();
        items.stream()
                .filter(item -> {
                    Order order = orderMap.get(item.getOrderId());
                    return order != null && userIds.contains(order.getUserId()) && !Objects.equals(item.getProductId(), productId);
                })
                .forEach(item -> scoreMap.merge(item.getProductId(), item.getQuantity() == null ? 1.0 : item.getQuantity().doubleValue(), Double::sum));

        Set<Long> browseUserIds = browseLogMapper.selectList(new LambdaQueryWrapper<BrowseLog>().eq(BrowseLog::getProductId, productId))
                .stream()
                .map(BrowseLog::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!browseUserIds.isEmpty()) {
            browseLogMapper.selectList(null).stream()
                    .filter(log -> log.getUserId() != null && browseUserIds.contains(log.getUserId()))
                    .filter(log -> !Objects.equals(log.getProductId(), productId))
                    .forEach(log -> scoreMap.merge(log.getProductId(), 0.3, Double::sum));
        }

        List<Product> result = productsByScore(scoreMap, size, Collections.singleton(productId));
        if (result.size() < size) {
            result.addAll(fallbackProducts(current.getCategoryId(), size - result.size(), excludedIds(result, productId)));
        }
        return result.stream().limit(size).collect(Collectors.toList());
    }

    @Override
    public List<Product> recommendForUser(Long userId, Integer limit) {
        int size = normalizeLimit(limit);
        if (userId == null) {
            return fallbackProducts(null, size, Collections.emptySet());
        }

        List<Order> userOrders = orderMapper.selectList(new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId));
        Set<Long> orderIds = userOrders.stream().map(Order::getId).collect(Collectors.toSet());
        List<OrderItem> boughtItems = orderItemMapper.selectList(null).stream()
                .filter(item -> orderIds.contains(item.getOrderId()))
                .collect(Collectors.toList());
        List<BrowseLog> browseLogs = browseLogMapper.selectList(new LambdaQueryWrapper<BrowseLog>().eq(BrowseLog::getUserId, userId));

        Set<Long> excluded = new HashSet<>();
        boughtItems.forEach(item -> excluded.add(item.getProductId()));
        browseLogs.forEach(log -> excluded.add(log.getProductId()));

        Map<Long, Product> productMap = productMapper.selectList(null).stream()
                .collect(Collectors.toMap(Product::getId, product -> product, (a, b) -> a));
        Set<Long> preferredCategories = new HashSet<>();
        boughtItems.forEach(item -> {
            Product product = productMap.get(item.getProductId());
            if (product != null && product.getCategoryId() != null) {
                preferredCategories.add(product.getCategoryId());
            }
        });
        browseLogs.forEach(log -> {
            if (log.getCategoryId() != null) {
                preferredCategories.add(log.getCategoryId());
            }
        });

        Map<Long, Double> scoreMap = new HashMap<>();
        productMap.values().stream()
                .filter(product -> product.getStatus() == null || product.getStatus() == 1)
                .filter(product -> !excluded.contains(product.getId()))
                .forEach(product -> {
                    if (preferredCategories.contains(product.getCategoryId())) {
                        scoreMap.merge(product.getId(), 1.0, Double::sum);
                    }
                });

        List<Product> result = productsByScore(scoreMap, size, excluded);
        if (result.size() < size) {
            result.addAll(fallbackProducts(null, size - result.size(), excludedIds(result, excluded)));
        }
        return result.stream().limit(size).collect(Collectors.toList());
    }

    private List<Product> productsByScore(Map<Long, Double> scoreMap, int limit, Set<Long> excluded) {
        return scoreMap.entrySet().stream()
                .filter(entry -> !excluded.contains(entry.getKey()))
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(entry -> productMapper.selectById(entry.getKey()))
                .filter(Objects::nonNull)
                .filter(product -> product.getStatus() == null || product.getStatus() == 1)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<Product> fallbackProducts(Long categoryId, int limit, Set<Long> excluded) {
        if (limit <= 0) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null, Product::getCategoryId, categoryId);
        wrapper.eq(Product::getStatus, 1);
        wrapper.notIn(!excluded.isEmpty(), Product::getId, excluded);
        wrapper.orderByDesc(Product::getCreateTime);
        wrapper.last("limit " + limit);
        return productMapper.selectList(wrapper);
    }

    private Set<Long> excludedIds(List<Product> products, Long extraId) {
        Set<Long> ids = products.stream().map(Product::getId).collect(Collectors.toSet());
        ids.add(extraId);
        return ids;
    }

    private Set<Long> excludedIds(List<Product> products, Set<Long> extraIds) {
        Set<Long> ids = products.stream().map(Product::getId).collect(Collectors.toSet());
        ids.addAll(extraIds);
        return ids;
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return 4;
        }
        return Math.min(limit, 20);
    }
}
