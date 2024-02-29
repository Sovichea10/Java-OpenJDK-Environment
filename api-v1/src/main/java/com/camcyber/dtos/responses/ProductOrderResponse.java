package com.camcyber.dtos.responses;

import lombok.Data;

@Data
public class ProductOrderResponse {
    private Integer id;
    private String name;
    private int quantity;
    private double unitPrice;
    private double discountPrice;
    private int discount;
    private double amount;
}
