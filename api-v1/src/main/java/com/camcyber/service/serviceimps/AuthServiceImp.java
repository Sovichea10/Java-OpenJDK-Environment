package com.camcyber.service.serviceimps;

import com.camcyber.dtos.requests.LoginRequest;
import com.camcyber.dtos.responses.LoginResponse;
import com.camcyber.entities.UserEntity;
import com.camcyber.repositories.UserRepository;
import com.camcyber.service.AuthService;
import com.camcyber.shares.JwtUtil;
import com.camcyber.shares.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByPhone(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid phone or password."));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));


        String accessToken = jwtUtil.generateToken(request.getUsername());

        LoginResponse response = new LoginResponse();

        response.setAccessToken(accessToken);
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setAvatar(null);

        return response;
    }
}
