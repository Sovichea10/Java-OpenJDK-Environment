package com.camcyber.service;

import com.camcyber.dtos.requests.ProductRequest;
import com.camcyber.dtos.requests.ProductTypeRequest;
import com.camcyber.dtos.responses.ProductResponse;
import com.camcyber.dtos.responses.ProductTypeResponseDetail;
import com.camcyber.dtos.responses.ProductTypeResponseList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductTypeService {

    void create (MultipartFile file,ProductTypeRequest requests);
    void update(MultipartFile file,Integer id,ProductTypeRequest request);

    List<ProductTypeResponseList> list ();
    ProductTypeResponseDetail detail(Integer id);
    void delete(Integer id);

}
