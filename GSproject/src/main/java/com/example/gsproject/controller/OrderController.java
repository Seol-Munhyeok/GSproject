package com.example.gsproject.controller;

import com.example.gsproject.dto.OrderDetailResponse;
import com.example.gsproject.dto.OrderRequestDTO;
import com.example.gsproject.entity.Order;
import com.example.gsproject.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO dto) {
        try {
            Order savedOrder = orderService.saveOrder(dto); // Order 리턴
            return ResponseEntity.ok().body(Map.of("orderId", savedOrder.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long id) {
        OrderDetailResponse response = orderService.getOrderDetail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderDetailResponse>> searchOrders(
            @RequestParam String name,
            @RequestParam String phone) {

        List<OrderDetailResponse> orders = orderService.getOrdersByCustomerInfo(name, phone);
        return ResponseEntity.ok(orders);
    }

}
