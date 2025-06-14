package com.example.gsproject.dto;

import java.util.List;

public record AdminStatisticsResponse(
        Summary summary,
        List<TopProduct> topProductsByQuantity,
        List<DailyStat> dailyStats
) {
    public record Summary(long totalOrderCount, long totalSales, long totalProvidedQuantity) {}
    public record TopProduct(Long productId, String productName, long totalQuantity, long totalSales) {}
    public record DailyStat(String date, long orderCount, long sales) {}
}