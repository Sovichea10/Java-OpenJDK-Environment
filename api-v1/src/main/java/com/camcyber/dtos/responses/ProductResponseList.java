package com.camcyber.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponseList {
    private int page;
    private int size;
    private int totalPage;
    private long totalSize;
    private List<ProductResponse> data;
}
