package com.exercie.exercies.controller;

import com.exercie.exercies.dto.request.UserDtoReq;
import com.exercie.exercies.dto.response.CommonResponse;
import com.exercie.exercies.dto.response.UserDtoRes;
import com.exercie.exercies.model.User;
import com.exercie.exercies.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> findAllUser(){
        List<UserDtoRes> data = authService.getAllUser();

        return CommonResponse.generateResponse(data, "Sucessfully get data", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id")Long  userId){
        UserDtoRes userDtoRes = authService.getUserById(userId);
        return CommonResponse.generateResponse(userDtoRes, "success", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> inserUser(@RequestBody UserDtoReq userDtoReq){
        authService.createUser(userDtoReq);
        return CommonResponse.generateResponse(userDtoReq, "success", HttpStatus.CREATED);
    }


}
