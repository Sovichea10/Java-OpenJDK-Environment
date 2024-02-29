package com.camcyber.rest_controller;

import com.camcyber.dtos.requests.ProductTypeRequest;
import com.camcyber.dtos.responses.BaseResponse;
import com.camcyber.dtos.responses.BaseResponseSuccess;
import com.camcyber.dtos.responses.ProductTypeResponseDetail;
import com.camcyber.dtos.responses.ProductTypeResponseList;
import com.camcyber.service.ProductTypeService;
import com.camcyber.shares.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/product-type")
@RequiredArgsConstructor
public class ProductTypeRestController {
    private final ProductTypeService productTypeService;

    @PostMapping(path = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> create(
            @RequestParam("file") MultipartFile file, ProductTypeRequest request){

        if (Objects.isNull(request.getName()))
            throw new BadRequestException("Product type's name is required.");

        productTypeService.create(file,request);

        return ResponseEntity.ok(new BaseResponseSuccess(HttpStatus.CREATED.value()));

    }
    @PutMapping(path = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> update(
            @RequestParam("file") MultipartFile file,
            @PathVariable Integer id,
            ProductTypeRequest request){
        productTypeService.update(file,id,request);
        return ResponseEntity.ok(new BaseResponseSuccess(HttpStatus.OK.value()));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<List<ProductTypeResponseList>>> listAll(){
        List<ProductTypeResponseList> productTypeResponses = productTypeService.list();
        BaseResponse<List<ProductTypeResponseList>> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(productTypeResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<ProductTypeResponseDetail>> detail(
            @PathVariable Integer id){

        ProductTypeResponseDetail productTypeResponse = productTypeService.detail(id);
        BaseResponse<ProductTypeResponseDetail> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(productTypeResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponseSuccess> delete(
            @PathVariable Integer id
    ){
        productTypeService.delete(id);
        return ResponseEntity.ok(new BaseResponseSuccess( ));
    }

}
