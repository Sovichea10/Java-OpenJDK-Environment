package com.camcyber.repositories;

import com.camcyber.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Integer> {
    FileEntity findByCode(String code);
}
