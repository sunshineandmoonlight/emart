package com.emart.modules.analysis.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.analysis.dto.*;
import com.emart.modules.analysis.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "数据分析", description = "用户画像、销售趋势、排行、库存和异常监控")
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Operation(summary = "数据分析总览")
    @GetMapping("/overview")
    public CommonResult<AnalysisOverviewDTO> overview() {
        return CommonResult.success(analysisService.getOverview());
    }

    @Operation(summary = "销售趋势")
    @GetMapping("/sales-trend")
    public CommonResult<List<SalesTrendPointDTO>> salesTrend(@RequestParam(defaultValue = "day") String range) {
        return CommonResult.success(analysisService.getSalesTrend(range));
    }

    @Operation(summary = "销售趋势预测与评估")
    @GetMapping("/sales-forecast")
    public CommonResult<SalesForecastDTO> salesForecast() {
        return CommonResult.success(analysisService.getSalesForecast());
    }

    @Operation(summary = "商品销售排行榜")
    @GetMapping("/product-ranking")
    public CommonResult<List<ProductRankingDTO>> productRanking(@RequestParam(defaultValue = "week") String range) {
        return CommonResult.success(analysisService.getProductRanking(range));
    }

    @Operation(summary = "分类销售统计")
    @GetMapping("/category-sales")
    public CommonResult<List<CategorySalesDTO>> categorySales() {
        return CommonResult.success(analysisService.getCategorySales());
    }

    @Operation(summary = "库存状态统计")
    @GetMapping("/stock-report")
    public CommonResult<List<StockReportDTO>> stockReport() {
        return CommonResult.success(analysisService.getStockReport());
    }

    @Operation(summary = "订单状态统计")
    @GetMapping("/order-status-report")
    public CommonResult<List<OrderStatusReportDTO>> orderStatusReport() {
        return CommonResult.success(analysisService.getOrderStatusReport());
    }

    @Operation(summary = "销售异常监控")
    @GetMapping("/anomalies")
    public CommonResult<List<SalesAnomalyDTO>> anomalies() {
        return CommonResult.success(analysisService.getAnomalies());
    }

    @Operation(summary = "用户画像详情")
    @GetMapping("/user-profile/{userId}")
    public CommonResult<UserProfileDTO> userProfile(@PathVariable Long userId) {
        UserProfileDTO profile = analysisService.getUserProfile(userId);
        if (profile == null) {
            return CommonResult.failed("用户不存在");
        }
        return CommonResult.success(profile);
    }

    @Operation(summary = "用户画像列表")
    @GetMapping("/user-profiles")
    public CommonResult<List<UserProfileDTO>> userProfiles() {
        return CommonResult.success(analysisService.listUserProfiles());
    }
}
