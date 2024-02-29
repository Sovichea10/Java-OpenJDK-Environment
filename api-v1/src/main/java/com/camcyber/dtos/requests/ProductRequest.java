package com.camcyber.dtos.requests;

import lombok.Data;

@Data
public class ProductRequest {
    private String code;
    private String name;
    private double unitPrice;
    private int discount;
    private String description;
    private int quantity;
    private Integer productType;
}
