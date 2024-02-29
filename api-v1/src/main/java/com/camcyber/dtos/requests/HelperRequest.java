package com.camcyber.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;

public class HelperRequest {
    private int page = 1;
    private int size = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy;
    private String keyword;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
