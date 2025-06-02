package com.example.gsproject.controller;

import com.example.gsproject.dto.OrderRequestDTO;
import com.example.gsproject.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO dto) {
        try {
            orderService.saveOrder(dto);
            return ResponseEntity.ok("주문 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("주문 실패: " + e.getMessage());
        }
    }
}
