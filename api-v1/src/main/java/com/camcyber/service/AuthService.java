package com.camcyber.service;

import com.camcyber.dtos.requests.LoginRequest;
import com.camcyber.dtos.responses.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}
