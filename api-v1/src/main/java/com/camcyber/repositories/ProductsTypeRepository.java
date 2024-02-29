package com.camcyber.repositories;

import com.camcyber.entities.ProductsTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsTypeRepository extends JpaRepository<ProductsTypeEntity,Integer> {
    Optional<ProductsTypeEntity> findByName(String name);
}
