package com.example.gsproject.dto;

import java.time.LocalDate;

public record DailyStatDTO(LocalDate date, long orderCount, long sales) {}
