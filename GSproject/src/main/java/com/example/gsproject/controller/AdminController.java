package com.example.gsproject.controller;

import com.example.gsproject.dto.AdminLoginRequest;
import com.example.gsproject.dto.AdminStatisticsResponse;
import com.example.gsproject.dto.OrderAdminViewDTO;
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
}
