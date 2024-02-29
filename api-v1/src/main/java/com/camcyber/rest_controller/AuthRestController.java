package com.camcyber.rest_controller;

import com.camcyber.dtos.requests.LoginRequest;
import com.camcyber.dtos.responses.BaseResponse;
import com.camcyber.dtos.responses.LoginResponse;
import com.camcyber.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login (LoginRequest request) throws BadRequestException {

        if (Objects.isNull(request)) throw new BadRequestException("Bad Request");
        if (Objects.isNull(request.getUsername()) || Objects.isNull(request.getPassword()))
            throw new BadRequestException("Bad Request");
        LoginResponse login = authService.login(request);

        BaseResponse<LoginResponse> response = new BaseResponse<>();
        response.setData(login);
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.ok(response);

    }

}
