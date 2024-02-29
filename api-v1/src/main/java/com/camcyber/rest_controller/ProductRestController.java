package com.camcyber.rest_controller;

import com.camcyber.dtos.requests.HelperRequest;
import com.camcyber.dtos.requests.ProductRequest;
import com.camcyber.dtos.responses.*;
import com.camcyber.service.ProductService;
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

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @PostMapping(path = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> create(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails,
            ProductRequest request){

        if (Objects.isNull(request.getCode()))
            throw new BadRequestException("Product's code is required.");
        if (Objects.isNull(request.getName()))
            throw new BadRequestException("Product's name is required.");
        if (Objects.isNull(request.getProductType()))
            throw new BadRequestException("Product's type is required.");

        productService.create(file,request,userDetails);

        return ResponseEntity.ok(new BaseResponseSuccess(HttpStatus.CREATED.value()));

    }
    @PutMapping(path = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> update(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id,
            ProductRequest request){
        productService.update(file,id,request,userDetails);
        return ResponseEntity.ok(new BaseResponseSuccess(HttpStatus.OK.value()));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<List<ProductResponse>>> listAll(){
        List<ProductResponse> productResponses = productService.list();
        BaseResponse<List<ProductResponse>> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(productResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<ProductResponse>> detail(
            @PathVariable Integer id){

        ProductResponse productResponse = productService.detail(id);
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(productResponse);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id
    ){
        productService.delete(id,userDetails);
        return ResponseEntity.ok(new BaseResponseSuccess( ));
    }

    @GetMapping( "/search")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<ProductResponseList>> filter(HelperRequest request){
        BaseResponse<ProductResponseList> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(productService.search(request));
        return ResponseEntity.ok(response);
    }

}
