package com.camcyber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
public class PosSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosSystemApplication.class, args);
    }

}
