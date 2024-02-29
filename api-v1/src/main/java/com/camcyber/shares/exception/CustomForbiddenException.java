package com.camcyber.shares.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomForbiddenException extends RuntimeException{
    CustomForbiddenException(String message){
        super(message);
    }
    public CustomForbiddenException(){}
}
