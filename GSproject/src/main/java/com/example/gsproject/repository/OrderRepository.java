package com.example.gsproject.repository;

import com.example.gsproject.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDate(LocalDate date);  // 직원용 오늘 주문 조회용
}
