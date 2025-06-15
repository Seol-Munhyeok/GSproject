package com.example.gsproject.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        LocalDateTime createdAt,
        int totalPrice,
        String customerName,
        List<OrderItemResponse> items
) {}
