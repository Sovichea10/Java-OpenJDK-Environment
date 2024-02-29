package com.camcyber.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private List<ProductOrderRequest> productIds;
    private Integer cashierId;
    private Integer discount;
    private double cash;
}
