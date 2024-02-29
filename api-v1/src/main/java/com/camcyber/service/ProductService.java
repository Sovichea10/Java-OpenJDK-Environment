package com.camcyber.service;

import com.camcyber.dtos.requests.HelperRequest;
import com.camcyber.dtos.requests.ProductRequest;
import com.camcyber.dtos.responses.ProductResponse;
import com.camcyber.dtos.responses.ProductResponseList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    void create (MultipartFile file,ProductRequest request, UserDetails userDetails);
    void update(MultipartFile file,Integer id, ProductRequest request, UserDetails userDetails);
    List<ProductResponse> list ();
    ProductResponse detail(Integer id);
    void delete(Integer id,UserDetails userDetails);
    ProductResponseList search(HelperRequest request);
}
