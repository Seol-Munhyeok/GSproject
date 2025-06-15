package com.example.gsproject.dto;

public record TopCustomerDTO(
        String name,
        String phone,
        int totalOrderCount,
        int totalSpent
) {}

