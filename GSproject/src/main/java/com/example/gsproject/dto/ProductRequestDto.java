package com.example.gsproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String name;
    private int price;
    private boolean availableToday;
    private String promotionType;
    private String description;
}
