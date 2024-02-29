package com.camcyber.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Integer id;
    private String code;
    private String name;
    private double unitPrice;
    private int discount;
    private String description;
    private int quantity;
    private String image;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
