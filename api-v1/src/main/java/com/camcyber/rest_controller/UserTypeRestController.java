package com.camcyber.rest_controller;

import com.camcyber.dtos.requests.UserTypeRequest;
import com.camcyber.dtos.responses.BaseResponse;
import com.camcyber.dtos.responses.BaseResponseSuccess;
import com.camcyber.dtos.responses.UserTypeResponse;
import com.camcyber.service.UserTypeService;
import com.camcyber.shares.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user-type")
@RequiredArgsConstructor
public class UserTypeRestController {
    private final UserTypeService userTypeService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> create(@RequestBody UserTypeRequest request){
        if (Objects.isNull(request.getName()))
            throw new BadRequestException("User type's name is required.");
        userTypeService.create(request);
        BaseResponseSuccess responseSuccess = new BaseResponseSuccess();
        responseSuccess.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.ok(responseSuccess);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> update(
            @PathVariable Integer id,
            @RequestBody UserTypeRequest request){
        if (Objects.isNull(request.getName()))
            throw new BadRequestException("User type's name is required.");
        userTypeService.update(id,request);
        return ResponseEntity.ok(new BaseResponseSuccess());
    }
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<UserTypeResponse>> detail(@PathVariable Integer id){
        BaseResponse<UserTypeResponse> response = new BaseResponse<>();
        response.setData(userTypeService.detail(id));
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<List<UserTypeResponse>>> list(){
        BaseResponse<List<UserTypeResponse>> response = new BaseResponse<>();
        response.setData(userTypeService.list());
        response.setStatus(HttpStatus.OK.value());
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> delete(@PathVariable Integer id){
        userTypeService.delete(id);
        return ResponseEntity.ok(new BaseResponseSuccess());
    }



}
