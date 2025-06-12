package com.example.gsproject.service;

import com.example.gsproject.entity.*;
import com.example.gsproject.dto.OrderRequestDTO;
import com.example.gsproject.dto.OrderItemDTO;
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
    public void saveOrder(OrderRequestDTO dto) {
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
    }
}
