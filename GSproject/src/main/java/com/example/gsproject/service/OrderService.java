package com.example.gsproject.service;

import com.example.gsproject.dto.*;
import com.example.gsproject.entity.*;
import com.example.gsproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(CustomerRepository customerRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order saveOrder(OrderRequestDTO dto) {
        // 1. 고객 저장
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setPhone(dto.phone());
        customer = customerRepository.save(customer);

        // 2. 주문 저장
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDate.now());
        order = orderRepository.save(order);

        orderRepository.flush();

        // 3. 주문 상세 저장
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDTO itemDTO : dto.orders()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID: " + itemDTO.productId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setQuantity(itemDTO.quantity());

            orderItemList.add(item);
        }

        orderItemRepository.saveAll(orderItemList);
        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderAdminViewDTO> getAdminOrders() {
        return orderRepository.findAllOrdersForAdmin();
    }

    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));

        List<OrderItemResponse> items = order.getOrderItems().stream().map(item ->
                new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                )
        ).toList();

        int totalPrice = items.stream()
                .mapToInt(i -> i.price() * i.quantity())
                .sum();

        return new OrderDetailResponse(
                order.getId(),
                order.getCreatedAt(),
                totalPrice,
                order.getCustomer().getName(),
                items
        );
    }

    public List<OrderDetailResponse> getOrdersByCustomerInfo(String name, String phone) {
        List<Customer> customers = customerRepository.findByNameAndPhone(name, phone);

        if (customers.isEmpty()) return List.of();

        List<OrderDetailResponse> result = new ArrayList<>();

        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                List<OrderItemResponse> items = order.getOrderItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity()
                        )).toList();

                int totalPrice = items.stream()
                        .mapToInt(i -> i.price() * i.quantity())
                        .sum();

                result.add(new OrderDetailResponse(
                        order.getId(),
                        order.getCreatedAt(),
                        totalPrice,
                        customer.getName(),
                        items
                ));
            }
        }

        return result;
    }




}
