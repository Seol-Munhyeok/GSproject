package com.example.gsproject.service;

import com.example.gsproject.entity.Product;
import com.example.gsproject.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getTodayProducts() {
        return productRepository.findByAvailableTodayTrue();
    }
}
