package com.camcyber.repositories;

import com.camcyber.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity> findByPhone(String phone);
    List<UserEntity> findAllByIsDeletedFalse();

    Optional<UserEntity> findByIdAndIsDeletedFalse(Integer id);

}
