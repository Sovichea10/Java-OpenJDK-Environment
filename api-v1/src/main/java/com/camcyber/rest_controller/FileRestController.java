package com.camcyber.rest_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR ;
    @GetMapping("/storage/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) {
        String contentType;
        Resource resource;
        try {
            // Resolve the file path using the UPLOAD_DIR and fileName
            Path rootPath = Paths.get(UPLOAD_DIR);
            Path file = rootPath.resolve(filename);

            // Create a UrlResource based on the file path
            resource = new UrlResource(file.toUri());

            // Obtain the content type of the file
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            // Handle IOException by throwing a RuntimeException
            throw new RuntimeException("Cannot read the file.");
        }

        // Return a ResponseEntity with the file content, headers, and status
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

}
