package com.example.gsproject.repository;

import com.example.gsproject.dto.DailyStatDTO;
import com.example.gsproject.dto.OrderAdminViewDTO;
import com.example.gsproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(LocalDate from, LocalDate to);
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

    @Query(value = """
    SELECT
        COUNT(DISTINCT o.id),
        COALESCE(SUM(p.price * oi.quantity), 0)
    FROM orders o
    JOIN order_item oi ON o.id = oi.order_id
    JOIN product p ON oi.product_id = p.id
    WHERE o.order_date BETWEEN :from AND :to
""", nativeQuery = true)
    List<Object[]> getSummary(@Param("from") LocalDate from, @Param("to") LocalDate to);


    @Query(value = """
        SELECT 
            o.order_date AS date,
            COUNT(DISTINCT o.id) AS order_count,
            SUM(p.price * oi.quantity) AS sales
        FROM orders o
        JOIN order_item oi ON o.id = oi.order_id
        JOIN product p ON oi.product_id = p.id
        WHERE o.order_date BETWEEN :from AND :to
        GROUP BY o.order_date
        ORDER BY o.order_date
    """, nativeQuery = true)
    List<Object[]> getDailyStats(@Param("from") LocalDate from, @Param("to") LocalDate to);

}
