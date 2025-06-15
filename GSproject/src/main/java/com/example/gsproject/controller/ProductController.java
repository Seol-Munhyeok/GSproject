package com.example.gsproject.controller;

import com.example.gsproject.dto.ProductRequestDto;
import com.example.gsproject.entity.Product;
import com.example.gsproject.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") ProductRequestDto dto,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        // 파일 저장
        String uploadDir = "uploads/";
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Product 저장
        Product product = new Product(
                dto.getName(),
                dto.getPrice(),
                dto.isAvailableToday(),
                dto.getPromotionType(),
                filename, // DB에는 파일명만 저장
                dto.getDescription()
        );
        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

}
