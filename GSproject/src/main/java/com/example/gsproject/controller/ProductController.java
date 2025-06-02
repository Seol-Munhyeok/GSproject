package com.example.gsproject.controller;

import com.example.gsproject.entity.Product;
import com.example.gsproject.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/today")
    public List<Product> getTodayProducts() {
        return productService.getTodayProducts();
    }
}
