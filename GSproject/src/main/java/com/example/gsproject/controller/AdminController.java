package com.example.gsproject.controller;

import com.example.gsproject.dto.*;
import com.example.gsproject.service.AdminService;
import com.example.gsproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        boolean isValid = adminService.login(request);
        if (isValid) {
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "로그인 성공"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "로그인 실패"
            ));
        }
    }

    // 관리자용 주문현황 API
    @GetMapping("/orders")
    public ResponseEntity<List<OrderAdminViewDTO>> getOrdersForAdmin() {
        List<OrderAdminViewDTO> orders = orderService.getAdminOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsResponse> getStatistics(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return ResponseEntity.ok(adminService.getStatistics(from, to));
    }

    @GetMapping("/customers")
    public List<CustomerStatDTO> getAllCustomers() {
        return adminService.getAllCustomerStats();
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordChangeRequest request) {
        try {
            adminService.updatePassword(request);
            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
