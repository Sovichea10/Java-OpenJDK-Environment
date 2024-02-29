package com.camcyber.service.serviceimps;

import com.camcyber.dtos.requests.ProductTypeRequest;
import com.camcyber.dtos.responses.ProductResponse;
import com.camcyber.dtos.responses.ProductTypeResponseDetail;
import com.camcyber.dtos.responses.ProductTypeResponseList;
import com.camcyber.entities.FileEntity;
import com.camcyber.entities.ProductEntity;
import com.camcyber.entities.ProductsTypeEntity;
import com.camcyber.repositories.FileRepository;
import com.camcyber.repositories.ProductsTypeRepository;
import com.camcyber.service.ProductService;
import com.camcyber.service.ProductTypeService;
import com.camcyber.shares.FileUtil;
import com.camcyber.shares.exception.BadRequestException;
import com.camcyber.shares.exception.InternalServerException;
import com.camcyber.shares.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImp implements ProductTypeService {

    private final ProductsTypeRepository productsTypeRepository;
    private final FileRepository fileRepository;
    @Value("${file.upload-dir}")
    private String UPLOAD_DIR ;

    @Override
    public void create(MultipartFile file,ProductTypeRequest request) {
        String uniqueCode = StringUtils.cleanPath(Objects.requireNonNull
                (Objects.requireNonNull(file.getOriginalFilename()).replaceFirst("[.][^.]+$", ""))) + "_" + System.currentTimeMillis();
        FileEntity fileEntity1;
        try {
            // Save the file to the specified storage directory
            Path filePath = Paths.get(UPLOAD_DIR + uniqueCode.concat(".".concat(Objects.requireNonNull(FileUtil.getFileExtension(file)))));
            Files.copy(file.getInputStream(), filePath);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(FileUtil.getFileExtension(file));
            fileEntity.setCode(uniqueCode);
            fileEntity1 = fileRepository.save(fileEntity);

        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error file upload failed.");
        }
        if (productsTypeRepository.findByName(request.getName()).isPresent())
            throw new BadRequestException("The product type's name is existed.");

        ProductsTypeEntity productsType = new ProductsTypeEntity();
        productsType.setIcon(fileEntity1);
        productsType.setName(request.getName());

        try {
            productsTypeRepository.save(productsType);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error.");
        }


    }

    @Override
    public void update(MultipartFile file,Integer id, ProductTypeRequest request) {

        ProductsTypeEntity productsType = productsTypeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("The product type not found."));
        if (Objects.equals(request.getName(),productsType.getName()))
            throw new BadRequestException("The product type's name is existed.");
        Boolean isUpdated = Boolean.FALSE;
        if (Objects.nonNull(file)){
            String uniqueCode = StringUtils.cleanPath(Objects.requireNonNull
                    (Objects.requireNonNull(file.getOriginalFilename()).replaceFirst("[.][^.]+$", ""))) + "_" + System.currentTimeMillis();
            FileEntity fileEntity1;
            try {
                // Save the file to the specified storage directory
                Path filePath = Paths.get(UPLOAD_DIR + uniqueCode.concat(".".concat(Objects.requireNonNull(FileUtil.getFileExtension(file)))));
                Files.copy(file.getInputStream(), filePath);

                FileEntity fileEntity = new FileEntity();
                fileEntity.setName(FileUtil.getFileExtension(file));
                fileEntity.setCode(uniqueCode);
                fileEntity1 = fileRepository.save(fileEntity);

            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalServerException("Internal Server Error file upload failed.");
            }
            productsType.setIcon(fileEntity1);
            isUpdated=Boolean.TRUE;
        }
        if (Objects.nonNull(request.getName())){
            productsType.setName(request.getName());
            isUpdated=Boolean.TRUE;
        }
        try {
            if (isUpdated){
                productsTypeRepository.save(productsType);
            }else throw new BadRequestException("Nothing updated.");

        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error.");
        }

    }

    @Override
    public List<ProductTypeResponseList> list() {

        List<ProductTypeResponseList> responseLists = new ArrayList<>();
        List<ProductsTypeEntity> productsTypes = productsTypeRepository.findAll();
        for (ProductsTypeEntity product : productsTypes){
            ProductTypeResponseList responseList = new ProductTypeResponseList();
            responseList.setName(product.getName());
            responseList.setId(product.getId());
            String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(product.getIcon().getCode().concat(".").concat(product.getIcon().getName()))
                    .toUriString();
            responseList.setIcon(downloadLink);
            responseLists.add(responseList);
        }

        return responseLists;
    }

    @Override
    public ProductTypeResponseDetail detail(Integer id) {
        ProductsTypeEntity productsType = productsTypeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("The product type not found."));
        ProductTypeResponseDetail responseDetail = new ProductTypeResponseDetail();
        responseDetail.setId(productsType.getId());
        responseDetail.setName(productsType.getName());
        String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(productsType.getIcon().getCode().concat(".").concat(productsType.getIcon().getName()))
                .toUriString();
        responseDetail.setIcon(downloadLink);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (ProductEntity product:productsType.getProducts()){
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setCode(product.getCode());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setQuantity(product.getQuantity());
            response.setUnitPrice(product.getUnitPrice());
            response.setDiscount(product.getDiscount());
            productResponses.add(response);
        }
        responseDetail.setProducts(productResponses);
        return responseDetail;
    }

    @Override
    public void delete(Integer id) {
        ProductsTypeEntity productsType = productsTypeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("The product type not found."));
        if (!productsType.getProducts().isEmpty())
            throw new InternalServerException("Can't delete.");
        productsTypeRepository.delete(productsType);

    }
}
