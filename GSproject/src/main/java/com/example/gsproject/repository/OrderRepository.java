package com.example.gsproject.repository;

import com.example.gsproject.dto.OrderAdminViewDTO;
import com.example.gsproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDate(LocalDate date);  // 직원용 오늘 주문 조회용
    @Query("""
    SELECT new com.example.gsproject.dto.OrderAdminViewDTO(
        o.customer.name,
        i.product.name,
        i.quantity
    )
    FROM Order o
    JOIN o.orderItems i
    ORDER BY o.customer.name ASC
    """)
    List<OrderAdminViewDTO> findAllOrdersForAdmin();

}
