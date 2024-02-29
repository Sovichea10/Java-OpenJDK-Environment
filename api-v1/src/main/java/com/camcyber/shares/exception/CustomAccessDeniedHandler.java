package com.camcyber.shares.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler extends Throwable implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("date", LocalDateTime.now());
        responseData.put("status", HttpServletResponse.SC_FORBIDDEN);
        responseData.put("error", "Forbidden.");
        responseData.put("message", "Access Denied.");

        OutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, responseData);
        out.flush();
    }



}
