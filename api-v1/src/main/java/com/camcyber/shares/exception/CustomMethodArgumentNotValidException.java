package com.camcyber.shares.exception;

public class CustomMethodArgumentNotValidException extends RuntimeException{
    CustomMethodArgumentNotValidException(String message){
        super(message);
    }
    CustomMethodArgumentNotValidException(){}
}

