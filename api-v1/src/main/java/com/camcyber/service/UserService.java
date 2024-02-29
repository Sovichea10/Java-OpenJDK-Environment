package com.camcyber.service;

import com.camcyber.dtos.requests.UserRequest;
import com.camcyber.dtos.responses.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void create(MultipartFile file,UserRequest request, UserDetails userDetails);
    List<UserResponse> list();
    UserResponse detail(Integer id);
    void update(MultipartFile file,Integer id,UserRequest request,UserDetails userDetails) throws IOException;
    void delete(Integer id,UserDetails userDetails);
}
