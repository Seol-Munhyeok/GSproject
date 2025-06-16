package com.example.gsproject.dto;

public record PasswordChangeRequest(
        String email,
        String currentPassword,
        String newPassword
) {}
