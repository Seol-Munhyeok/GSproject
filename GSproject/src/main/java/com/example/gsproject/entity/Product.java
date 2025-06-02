package com.example.gsproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private boolean availableToday;  // 오늘 사전예약 가능한지 여부

    private String promotionType;  // 예: "1+1", "카드결제할인", null

    public Product(String name, int price, boolean availableToday, String promotionType) {
        this.name = name;
        this.price = price;
        this.availableToday = availableToday;
        this.promotionType = promotionType;
    }


}