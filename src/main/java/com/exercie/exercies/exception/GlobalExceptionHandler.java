package com.exercie.exercies.exception;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generateGeneralError(Exception e){
        return generateResponseError(e.getMessage(), "something went wrong", HttpStatus.BAD_REQUEST, "UN_TRACK_ERROR");
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> generateResponseNotFound(Exception e){
        return generateResponseError(e.getMessage(), "Resources Not Found", HttpStatus.NOT_FOUND, "NOT_FOUND_ERROR");
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> generateResponseNotFound(BadRequestException e){
        return generateResponseError(e.getMessage(), "Bad request", HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> generateResponseNotFound(DuplicateKeyException e){
        return generateResponseError("Data already exists.", "Bad request", HttpStatus.BAD_REQUEST, "DB_DUPLICATE_ENTRY_ERROR");
    }

    private ResponseEntity<?> generateResponseError(Object errors, String message, HttpStatus status){
        Map<String, Object> res = new HashMap<>();
        res.put("errors", errors);
        res.put("message", message);
        res.put("status", status.value());
        res.put("success", false);
        res.put("data", null);
        return new ResponseEntity<>(res, status);
    }

    private ResponseEntity<?> generateResponseError(Object errors, String message, HttpStatus status, String errorCode){
        Map<String, Object> res = new HashMap<>();
        res.put("errors", errors);
        res.put("message", message);
        res.put("status", status.value());
        res.put("success", false);
        res.put("data", null);
        res.put("errorCode", errorCode);

        return new ResponseEntity<>(res, status);
    }

}
