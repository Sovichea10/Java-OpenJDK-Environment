package com.camcyber.dtos.responses;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String avatar;
}
