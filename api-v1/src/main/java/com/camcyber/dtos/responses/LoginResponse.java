package com.camcyber.dtos.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String name;
    private String phone;
    private String email;
    private String avatar;
}
