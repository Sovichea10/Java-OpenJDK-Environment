package com.camcyber.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseResponse<T> {
    private Integer status;
    public String message = "Success";
    private LocalDateTime date = LocalDateTime.now();
    T data;
}
