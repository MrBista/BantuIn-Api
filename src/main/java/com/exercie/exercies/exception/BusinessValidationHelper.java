package com.exercie.exercies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessValidationHelper extends RuntimeException{
    public BusinessValidationHelper(String message) {
        super(message);
    }

}
