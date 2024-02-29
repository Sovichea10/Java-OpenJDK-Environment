package com.camcyber.service;

import com.camcyber.shares.exception.InternalServerException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    void uploadImage(MultipartFile file) throws IOException;

}
