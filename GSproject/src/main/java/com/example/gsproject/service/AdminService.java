package com.example.gsproject.service;

import com.example.gsproject.dto.AdminLoginRequest;
import com.example.gsproject.entity.AdminUser;
import com.example.gsproject.repository.AdminUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminUserRepository adminUserRepository;

    public AdminService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    public boolean login(AdminLoginRequest request) {
        return adminUserRepository.findByEmail(request.email())
                .map(user -> user.getPassword().equals(request.password()))
                .orElse(false);
    }
}
