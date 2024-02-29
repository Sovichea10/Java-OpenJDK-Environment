package com.camcyber.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseSuccess {
    private Integer status;
    public String message = "Success";
    private LocalDateTime date = LocalDateTime.now();

    public BaseResponseSuccess(Integer status) {
        this.status = status;
    }
}
