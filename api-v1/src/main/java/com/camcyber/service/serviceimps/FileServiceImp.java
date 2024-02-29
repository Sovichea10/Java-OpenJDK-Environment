package com.camcyber.service.serviceimps;

import com.camcyber.service.FileService;
import com.camcyber.shares.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class FileServiceImp implements FileService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public void uploadImage(MultipartFile file) throws IOException {
        // Generate a unique code for the file
        String uniqueCode = UUID.randomUUID().toString();

        // Get the file extension
        String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));

        // Build the file path
        String filePath = uploadDir + File.separator + uniqueCode + "." + fileExtension;
        file.transferTo(new File(filePath));

    }
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
