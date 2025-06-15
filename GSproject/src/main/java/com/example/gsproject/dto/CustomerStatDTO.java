package com.example.gsproject.dto;

public record CustomerStatDTO(
        String name,
        String phone,
        int totalOrderCount,
        int totalSpent
) {}

