package com.example.gsproject.dto;

import java.util.List;

public record OrderRequestDTO(
        String name,
        String phone,
        List<OrderItemDTO> orders
) {}
