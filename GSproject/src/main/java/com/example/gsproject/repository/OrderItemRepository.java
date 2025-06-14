package com.example.gsproject.repository;

import com.example.gsproject.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :from AND :to")
    List<OrderItem> findAllByOrderDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = """
        SELECT 
            p.id AS product_id,
            p.name AS product_name,
            SUM(oi.quantity) AS total_quantity,
            SUM(p.price * oi.quantity) AS total_sales
        FROM order_item oi
        JOIN product p ON oi.product_id = p.id
        JOIN orders o ON oi.order_id = o.id
        WHERE o.order_date BETWEEN :from AND :to
        GROUP BY p.id, p.name
        ORDER BY total_quantity DESC
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getTopProducts(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
