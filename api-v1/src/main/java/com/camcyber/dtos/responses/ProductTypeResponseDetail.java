package com.camcyber.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class ProductTypeResponseDetail {
    private Integer id;
    private String name;
    private String icon;
    private List<ProductResponse> products;
}
