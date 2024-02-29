package com.camcyber.rest_controller;

import com.camcyber.dtos.requests.UserRequest;
import com.camcyber.dtos.responses.BaseResponse;
import com.camcyber.dtos.responses.BaseResponseSuccess;
import com.camcyber.dtos.responses.UserResponse;
import com.camcyber.service.UserService;
import com.camcyber.shares.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @PostMapping(path = "/create",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> create(
            @RequestParam("file") MultipartFile file,
           @AuthenticationPrincipal UserDetails userDetails,
            UserRequest request){

        if (Objects.isNull(request.getName()))
            throw new BadRequestException("Name is required.");
        if (Objects.isNull(request.getPhone()))
            throw new BadRequestException("Phone number is required.");
        if (Objects.isNull(request.getPassword()))
            throw new BadRequestException("Password is required.");
        if (Objects.isNull(request.getUserType()))
            throw new BadRequestException("User type is required.");
        if (Objects.isNull(request.getEmail()))
            throw new BadRequestException("Email is required.");
//        if (EMAIL_PATTERN.matcher(request.getEmail()).matches())
//            throw new BadRequestException("Email is invalided.");

        userService.create(file,request,userDetails);

        return ResponseEntity.ok(new BaseResponseSuccess());
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<List<UserResponse>>> listAll(){
        List<UserResponse> userResponses = userService.list();
        BaseResponse<List<UserResponse>> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(userResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<UserResponse>> detail(
            @PathVariable Integer id){

        UserResponse userResponse = userService.detail(id);
        BaseResponse<UserResponse> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(userResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> update(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id,
            UserRequest userRequest
    ) throws IOException {
        if (EMAIL_PATTERN.matcher(userRequest.getEmail()).matches())
            throw new BadRequestException("Email is invalided.");
        userService.update(file,id,userRequest,userDetails);
        return ResponseEntity.ok(new BaseResponseSuccess());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id
    ){
        userService.delete(id,userDetails);
        return ResponseEntity.ok(new BaseResponseSuccess( ));
    }


}
