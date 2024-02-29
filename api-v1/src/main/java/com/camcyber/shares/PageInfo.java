package com.camcyber.shares;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageInfo {
    private int currentPage;
    private int currentSize;
    private int totalPages;
    private int totalSize;

    public PageInfo() {
    }
}