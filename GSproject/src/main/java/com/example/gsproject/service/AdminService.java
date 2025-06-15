package com.example.gsproject.service;

import com.example.gsproject.dto.AdminLoginRequest;
import com.example.gsproject.dto.AdminStatisticsResponse;
import com.example.gsproject.dto.CustomerStatDTO;
import com.example.gsproject.dto.TopCustomerDTO;
import com.example.gsproject.entity.Order;
import com.example.gsproject.entity.OrderItem;
import com.example.gsproject.repository.AdminUserRepository;
import com.example.gsproject.repository.OrderItemRepository;
import com.example.gsproject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {

    private final AdminUserRepository adminUserRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminService(AdminUserRepository adminUserRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.adminUserRepository = adminUserRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public boolean login(AdminLoginRequest request) {
        return adminUserRepository.findByEmail(request.email())
                .map(user -> user.getPassword().equals(request.password()))
                .orElse(false);
    }

    public AdminStatisticsResponse getStatistics(LocalDate from, LocalDate to) {
        // 1. 요약: 주문 수 / 매출은 쿼리로
        Object[] row = orderRepository.getSummary(from, to).get(0);
        long totalOrderCount = ((Number) row[0]).longValue();
        long totalSales = ((Number) row[1]).longValue();


        // 1-1. 제공 예정 수량은 직접 계산
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderDateBetween(from, to);
        long totalProvidedQuantity = 0;

        for (OrderItem item : orderItems) {
            String promo = item.getProduct().getPromotionType();
            int quantity = item.getQuantity();
            if ("1+1".equals(promo)) {
                totalProvidedQuantity += quantity * 2L;
            } else {
                totalProvidedQuantity += quantity;
            }
        }

        // 2. 인기 상품
        List<Object[]> topProductRows = orderItemRepository.getTopProducts(from, to);
        List<AdminStatisticsResponse.TopProduct> topProducts = topProductRows.stream()
                .map(r -> new AdminStatisticsResponse.TopProduct(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        ((Number) r[2]).longValue(),
                        ((Number) r[3]).longValue()
                )).toList();

        // 3. 일별 통계
        List<Object[]> dailyRows = orderRepository.getDailyStats(from, to);
        List<AdminStatisticsResponse.DailyStat> dailyStats = dailyRows.stream()
                .map(r -> new AdminStatisticsResponse.DailyStat(
                        r[0].toString(),
                        ((Number) r[1]).longValue(),
                        ((Number) r[2]).longValue()
                )).toList();

        return new AdminStatisticsResponse(
                new AdminStatisticsResponse.Summary(totalOrderCount, totalSales, totalProvidedQuantity),
                topProducts,
                dailyStats
        );
    }

    public List<CustomerStatDTO> getAllCustomerStats() {
        return orderRepository.getCustomerStatsRaw().stream()
                .map(row -> new CustomerStatDTO(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).intValue(),
                        ((Number) row[3]).intValue()
                ))
                .toList();
    }








}
