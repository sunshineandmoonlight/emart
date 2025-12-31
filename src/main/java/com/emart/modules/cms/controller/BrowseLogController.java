package com.emart.modules.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.cms.model.BrowseLog;
import com.emart.modules.cms.service.BrowseLogService;
import com.emart.modules.pms.model.Product;
import com.emart.modules.pms.service.ProductService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 浏览日志Controller
 */
@Slf4j
@RestController
@Tag(name = "浏览日志管理", description = "用户浏览行为记录")
@RequestMapping("/browse")
public class BrowseLogController {

    @Autowired
    private BrowseLogService browseLogService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "记录商品浏览", hidden = true)
    @PostMapping("/log/{productId}")
    public CommonResult<Boolean> logBrowse(@PathVariable Long productId,
                                          HttpServletRequest request) {
        // 获取用户信息
        Long userId = null;
        String userName = null;
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                userId = jwtTokenUtil.getUserIdFromToken(token);
                userName = jwtTokenUtil.getUserNameFromToken(token);
            } catch (Exception e) {
                log.warn("解析Token失败，记录为匿名浏览");
            }
        }

        // 获取商品信息
        Product product = productService.getProductDetail(productId);
        if (product == null) {
            return CommonResult.failed("商品不存在");
        }

        // 获取IP
        String ip = getClientIp(request);

        // 记录浏览日志
        boolean success = browseLogService.logBrowse(userId, userName, productId, product.getName(), ip);
        return CommonResult.success(success);
    }

    @Operation(summary = "获取浏览日志列表（管理员）")
    @GetMapping("/logs")
    public CommonResult<Page<BrowseLog>> listBrowseLogs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BrowseLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BrowseLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BrowseLog::getCreateTime);
        Page<BrowseLog> logPage = browseLogService.page(page, wrapper);
        return CommonResult.success(logPage);
    }

    @Operation(summary = "获取热门商品（管理员）")
    @GetMapping("/hot")
    public CommonResult<List<Map<String, Object>>> getHotProducts() {
        // 查询所有浏览日志
        LambdaQueryWrapper<BrowseLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BrowseLog::getCreateTime);
        List<BrowseLog> allLogs = browseLogService.list(wrapper);

        // 统计每个商品的浏览次数
        Map<String, Map<String, Object>> productStats = allLogs.stream()
            .collect(Collectors.groupingBy(
                BrowseLog::getProductName,
                Collectors.collectingAndThen(
                    Collectors.counting(),
                    count -> {
                        Map<String, Object> stat = new java.util.HashMap<>();
                        stat.put("count", count);
                        return stat;
                    }
                )
            ));

        // 转换为列表并排序
        List<Map<String, Object>> hotProducts = productStats.entrySet().stream()
            .map(entry -> {
                Map<String, Object> product = new java.util.HashMap<>();
                product.put("productName", entry.getKey());
                product.put("viewCount", entry.getValue().get("count"));
                return product;
            })
            .sorted((a, b) -> ((Long) b.get("viewCount")).compareTo((Long) a.get("viewCount")))
            .limit(10)
            .collect(Collectors.toList());

        return CommonResult.success(hotProducts);
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
