package com.exercie.exercies.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class CommonResponse {

    public static ResponseEntity<?> generateResponse(Object data, String message, HttpStatus status){
        Map<String, Object> res = new HashMap<>();
        res.put("message", message);
        res.put("data", data);
        res.put("status", status.value());
        return new ResponseEntity<>(res, status);
    }
}
