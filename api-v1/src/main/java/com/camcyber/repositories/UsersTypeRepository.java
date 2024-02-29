package com.camcyber.repositories;

import com.camcyber.entities.UsersTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersTypeRepository extends JpaRepository<UsersTypeEntity,Integer> {

    Boolean existsByName(String name);

}
