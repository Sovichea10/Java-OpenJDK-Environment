package com.camcyber.shares.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final ResponseEntity<ErrorResponse> handleAllException(Exception exception,
                                                                  final HttpServletRequest request){
        ErrorResponse response = new ErrorResponse();
        response.setCode("500");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {CustomForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<ErrorResponse> forbiddenException(final CustomForbiddenException exception,
                                                                               final HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setCode("403");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = {CustomMethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(CustomMethodArgumentNotValidException exception,
                                                                               final HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setCode("400");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<ErrorResponse> unauthorizedException(final UnauthorizedException exception,
                                                               final HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setCode("401");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> notFoundException(final NotFoundException exception, final HttpServletRequest request){

        ErrorResponse response = new ErrorResponse();
        response.setCode("404");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setPath(request.getRequestURI());

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> badRequestException(final BadRequestException exception, final HttpServletRequest request){

        ErrorResponse response = new ErrorResponse();
        response.setCode("400");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setPath(request.getRequestURI());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> internalServerErrorException(final InternalServerException exception, final HttpServletRequest request){

        ErrorResponse response = new ErrorResponse();
        response.setCode("500");
        response.setDate(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        response.setError(exception.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setPath(request.getRequestURI());

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);

    }


}

