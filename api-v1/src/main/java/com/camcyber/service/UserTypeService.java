package com.camcyber.service;

import com.camcyber.dtos.requests.UserRequest;
import com.camcyber.dtos.requests.UserTypeRequest;
import com.camcyber.dtos.responses.UserTypeResponse;

import java.util.List;

public interface UserTypeService {

    void create(UserTypeRequest request);
    void update(Integer id, UserTypeRequest request);
    UserTypeResponse detail(Integer id);
    List<UserTypeResponse> list();
    void delete(Integer id);


}
