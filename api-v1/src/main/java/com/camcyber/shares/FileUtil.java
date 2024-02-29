package com.camcyber.shares;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return null;
    }
}
