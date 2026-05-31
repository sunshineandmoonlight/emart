package com.emart.modules.analysis.service;

import com.emart.modules.analysis.dto.*;

import java.util.List;

public interface AnalysisService {

    AnalysisOverviewDTO getOverview();

    List<SalesTrendPointDTO> getSalesTrend(String range);

    SalesForecastDTO getSalesForecast();

    List<ProductRankingDTO> getProductRanking(String range);

    List<CategorySalesDTO> getCategorySales();

    List<StockReportDTO> getStockReport();

    List<OrderStatusReportDTO> getOrderStatusReport();

    List<SalesAnomalyDTO> getAnomalies();

    UserProfileDTO getUserProfile(Long userId);

    List<UserProfileDTO> listUserProfiles();
}
