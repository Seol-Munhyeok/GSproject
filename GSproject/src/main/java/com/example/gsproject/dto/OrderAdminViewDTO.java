package com.example.gsproject.dto;

public record OrderAdminViewDTO(
        String customerName,
        String productName,
        int quantity
) {}
