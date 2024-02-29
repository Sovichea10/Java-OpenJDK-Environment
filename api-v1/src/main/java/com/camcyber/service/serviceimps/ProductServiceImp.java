package com.camcyber.service.serviceimps;

import com.camcyber.dtos.requests.HelperRequest;
import com.camcyber.dtos.requests.ProductRequest;
import com.camcyber.dtos.responses.ProductResponse;
import com.camcyber.dtos.responses.ProductResponseList;
import com.camcyber.entities.FileEntity;
import com.camcyber.entities.ProductEntity;
import com.camcyber.entities.ProductsTypeEntity;
import com.camcyber.repositories.FileRepository;
import com.camcyber.repositories.ProductRepository;
import com.camcyber.repositories.ProductsTypeRepository;
import com.camcyber.repositories.UserRepository;
import com.camcyber.service.ProductService;
import com.camcyber.service.Specification.ProductSpec;
import com.camcyber.service.Specification.ProductSpecification;
import com.camcyber.shares.FileUtil;
import com.camcyber.shares.PageInfo;
import com.camcyber.shares.PaginationUtil;
import com.camcyber.shares.exception.BadRequestException;
import com.camcyber.shares.exception.InternalServerException;
import com.camcyber.shares.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductsTypeRepository productsTypeRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final PaginationUtil<ProductEntity> paginationUtilService;
    @Value("${file.upload-dir}")
    private String UPLOAD_DIR ;

    @Override
    public void create(MultipartFile file, ProductRequest request, UserDetails userDetails) {
        if (productRepository.findByCodeAndIsDeletedFalse(request.getCode()).isPresent())
            throw new  BadRequestException("The code already existed.");
        String uniqueCode = StringUtils.cleanPath(Objects.requireNonNull
                (Objects.requireNonNull(file.getOriginalFilename()).replaceFirst("[.][^.]+$", ""))) + "_" + System.currentTimeMillis();
        FileEntity fileEntity1;
        try {
            // Save the file to the specified storage directory
            Path filePath = Paths.get(UPLOAD_DIR +
                    uniqueCode.concat(".".concat(Objects.requireNonNull(FileUtil.getFileExtension(file)))));
            Files.copy(file.getInputStream(), filePath);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(FileUtil.getFileExtension(file));
            fileEntity.setCode(uniqueCode);
            fileEntity1 = fileRepository.save(fileEntity);

        } catch (IOException e) {
            throw new InternalServerException("Internal Server Error file upload failed.");
        }
        ProductEntity product = new ProductEntity();
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setDiscount(request.getDiscount());
        product.setQuantity(request.getQuantity());
        product.setIsDeleted(Boolean.FALSE);
        product.setImage(fileEntity1);
        product.setCreator(userRepository.findByPhone(userDetails.getUsername())
                .orElseThrow(()->new NotFoundException("User not found.")));
        product.setCreatedAt(LocalDateTime.now());
        product.setProductsType(productsTypeRepository.findById(request.getProductType())
                .orElseThrow(()->new NotFoundException("The product type not found.")));
        try {
            productRepository.save(product);
        }catch (Exception e){
            throw new InternalServerException("Internal Server Error");
        }
    }

    @Override
    public void update(MultipartFile file,Integer id, ProductRequest request, UserDetails userDetails) {

        ProductEntity product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NotFoundException("The product not found."));
        Boolean isUpdated = Boolean.FALSE;

        if (Objects.nonNull(request.getCode()) &&
                productRepository.findByCodeAndIsDeletedFalse(request.getCode()).isEmpty()){
            product.setCode(request.getCode());
            isUpdated = Boolean.TRUE;
        }
        if (Objects.nonNull(request.getName())){
            product.setName(request.getName());
            isUpdated = Boolean.TRUE;
        }
        if (Objects.nonNull(request.getDescription())){
            product.setDescription(request.getDescription());
            isUpdated = Boolean.TRUE;
        }
        if (request.getUnitPrice()!=0){
            product.setUnitPrice(request.getUnitPrice());
            isUpdated = Boolean.TRUE;
        }
        if (request.getQuantity()!=0){
            product.setQuantity(request.getQuantity());
            isUpdated = Boolean.TRUE;
        }
        if (request.getDiscount()!=0){
            product.setDiscount(request.getDiscount());
            isUpdated = Boolean.TRUE;
        }
        if (Objects.nonNull(file)){
            String uniqueCode = StringUtils.cleanPath(Objects.requireNonNull
                    (Objects.requireNonNull(file.getOriginalFilename()).replaceFirst("[.][^.]+$", ""))) + "_" + System.currentTimeMillis();
            FileEntity fileEntity1;
            try {
                // Save the file to the specified storage directory
                Path filePath = Paths.get(UPLOAD_DIR +
                        uniqueCode.concat(".".concat(Objects.requireNonNull(FileUtil.getFileExtension(file)))));
                Files.copy(file.getInputStream(), filePath);

                FileEntity fileEntity = new FileEntity();
                fileEntity.setName(FileUtil.getFileExtension(file));
                fileEntity.setCode(uniqueCode);
                fileEntity1 = fileRepository.save(fileEntity);

            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalServerException("Internal Server Error file upload failed.");
            }
            product.setImage(fileEntity1);
            isUpdated = Boolean.TRUE;
        }
        if (Objects.nonNull(request.getProductType())){
            ProductsTypeEntity productsType = productsTypeRepository.findById(request.getProductType())
                            .orElseThrow(()->new NotFoundException("The product type not found."));
            product.setProductsType(productsType);
            isUpdated = Boolean.TRUE;
        }
        product.setUpdater(userRepository.findByPhone(userDetails.getUsername())
                .orElseThrow(()->new NotFoundException("User not found.")));
        product.setUpdatedAt(LocalDateTime.now());

        try {
            if (isUpdated){
                productRepository.save(product);
            }else throw new  BadRequestException("Nothing updated.");

        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error");
        }

    }

    @Override
    public List<ProductResponse> list() {

        List<ProductResponse> productResponses = new ArrayList<>();
        List<ProductEntity> productEntities  = productRepository.findAllByIsDeletedFalse();
        for (ProductEntity product: productEntities){
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setCode(product.getCode());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setQuantity(product.getQuantity());
            response.setUnitPrice(product.getUnitPrice());
            response.setDiscount(product.getDiscount());
            response.setCreatedDate(product.getCreatedAt());
            response.setUpdatedDate(product.getUpdatedAt());
            String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(product.getImage().getCode().concat(".").concat(product.getImage().getName()))
                    .toUriString();
            response.setImage(downloadLink);
            productResponses.add(response);
        }

        return productResponses;
    }
    @Override
    public ProductResponse detail(Integer id) {

        ProductEntity product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NotFoundException("The product not found."));
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setQuantity(product.getQuantity());
        response.setUnitPrice(product.getUnitPrice());
        response.setDiscount(product.getDiscount());
        response.setCreatedDate(product.getCreatedAt());
        response.setUpdatedDate(product.getUpdatedAt());
        String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(product.getImage().getCode().concat(".").concat(product.getImage().getName()))
                .toUriString();
        response.setImage(downloadLink);

        return response;
    }
    @Override
    public void delete(Integer id, UserDetails userDetails) {
        ProductEntity product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NotFoundException("The product not found."));
        product.setIsDeleted(Boolean.TRUE);
        product.setDeleter(userRepository.findByPhone(userDetails.getUsername())
                .orElseThrow(()->new NotFoundException("User not found.")));
        product.setDeletedAt(LocalDateTime.now());
        try {
            productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error");
        }

    }
    @Override
    public ProductResponseList search(HelperRequest request) {

        if (request.getPage()<=0) request.setPage(1);
        if (request.getSize()<=0) request.setSize(10);

        PageRequest pageRequest = PageRequest.of(request.getPage()-1, request.getSize());

        Specification<ProductEntity> specification = ProductSpec.getWithSearch(request);

        // Corrected the assignment of 'size' to 'currentSize'
        Page<ProductEntity> productEntities = productRepository.findAll(specification,pageRequest);

        PaginationUtil<ProductEntity> paginationUtil = paginationUtilService.calculatePageInfo(productEntities);

        List<ProductResponse> productResponses = new ArrayList<>();
        ProductResponseList responseList = new ProductResponseList();

        // Corrected the method used to set 'currentSize'
        responseList.setPage(paginationUtil.getPageInfo().getCurrentPage());
        responseList.setSize(paginationUtil.getPageInfo().getCurrentSize());
        responseList.setTotalPage(paginationUtil.getPageInfo().getTotalPages());
        responseList.setTotalSize(paginationUtil.getPageInfo().getTotalSize());

        for (ProductEntity product : paginationUtil.getPage().getContent()) {
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setCode(product.getCode());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setQuantity(product.getQuantity());
            response.setUnitPrice(product.getUnitPrice());
            response.setDiscount(product.getDiscount());
            response.setCreatedDate(product.getCreatedAt());
            response.setUpdatedDate(Objects.isNull(product.getUpdatedAt())?null:product.getUpdatedAt());

            // Adjusted the creation of the download link
            String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/storage/")
                    .path(product.getImage().getCode().concat(".").concat(product.getImage().getName()))
                    .toUriString();
            response.setImage(downloadLink);

            productResponses.add(response);
        }

        responseList.setData(productResponses);
        return responseList;
    }

}
