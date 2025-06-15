package com.example.gsproject.dto;

public record OrderItemResponse(
        String productName,
        int price,
        int quantity
) {}