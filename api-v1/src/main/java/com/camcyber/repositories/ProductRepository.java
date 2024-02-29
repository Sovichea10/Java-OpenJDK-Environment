package com.camcyber.repositories;

import com.camcyber.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<ProductEntity,Integer> {

    Optional<ProductEntity> findByCodeAndIsDeletedFalse(String code);
    Optional<ProductEntity> findByIdAndIsDeletedFalse(Integer id);
    List<ProductEntity> findAllByIsDeletedFalse();
    @Query(value = "SELECT * from product where quantity <=5", nativeQuery = true)
    List<ProductEntity> listAllNearOutStock();
    Page<ProductEntity> findAll(Specification<ProductEntity> specification,Pageable pageable);
}
