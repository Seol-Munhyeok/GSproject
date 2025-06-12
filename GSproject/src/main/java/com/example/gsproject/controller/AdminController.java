package com.example.gsproject.controller;

import com.example.gsproject.dto.AdminLoginRequest;
import com.example.gsproject.entity.AdminUser;
import com.example.gsproject.repository.AdminUserRepository;
import com.example.gsproject.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUserRepository adminUserRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody AdminLoginRequest request) {
        Optional<AdminUser> userOpt = adminUserRepository.findByEmail(request.email());

        Map<String, Object> response = new HashMap<>();

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.password())) {
            // 로그인 성공
            response.put("success", true);
            response.put("message", "로그인 성공");
            return ResponseEntity.ok(response);
        } else {
            // 로그인 실패
            response.put("success", false);
            response.put("message", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.ok(response);
        }
    }
}

