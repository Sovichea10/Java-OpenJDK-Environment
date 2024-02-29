package com.camcyber.dtos.requests;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String phone;
    private String email;
    private String password;
    private Integer userType;
}
